from typing import Optional
import uuid

from fastapi import FastAPI, HTTPException, Request, Response
from pydantic import BaseModel

from app.config import settings
from app.queue import publish_report_request
from app.db import get_greenhouse_name, load_register_data
import pandas as pd
from typing import List, Optional


class DailyValue(BaseModel):
    date: str
    value: Optional[float]


class DataResponse(BaseModel):
    greenhouse_id: int
    greenhouse_name: Optional[str]
    values: List[DailyValue]


class RangeItem(BaseModel):
    timestamp: str
    temperature: Optional[float]


class RangeResponse(BaseModel):
    greenhouse_id: int
    greenhouse_name: Optional[str]
    values: List[RangeItem]

app = FastAPI(title="Analytics Service")

jobs = {}


class ReportRequestPayload(BaseModel):
    greenhouse_id: int


class JobStatusResponse(BaseModel):
    job_id: str
    status: str
    message: Optional[str] = None


@app.get("/health")
def healthcheck():
    return {"status": "ok"}


@app.get("/data/temperature/{greenhouse_id}", response_model=DataResponse)
def get_temperature_data(greenhouse_id: int):
    name = get_greenhouse_name(greenhouse_id)
    df = load_register_data(greenhouse_id, days=30)

    end_date = pd.Timestamp.now().normalize()
    start_date = end_date - pd.Timedelta(days=29)
    date_range = pd.date_range(start=start_date, end=end_date, freq="D")

    if df.empty:
        series = pd.Series(index=date_range, dtype=float)
    else:
        df["time_stamp"] = pd.to_datetime(df["time_stamp"])
        series = (
            df.groupby(df["time_stamp"].dt.normalize())["temperature"].mean().reindex(date_range)
        )

    values = [
        DailyValue(date=d.strftime("%Y-%m-%d"), value=(None if pd.isna(v) else float(v)))
        for d, v in series.items()
    ]

    return DataResponse(greenhouse_id=greenhouse_id, greenhouse_name=name, values=values)


@app.get("/data/humidity/{greenhouse_id}", response_model=DataResponse)
def get_humidity_data(greenhouse_id: int):
    name = get_greenhouse_name(greenhouse_id)
    df = load_register_data(greenhouse_id, days=30)

    end_date = pd.Timestamp.now().normalize()
    start_date = end_date - pd.Timedelta(days=29)
    date_range = pd.date_range(start=start_date, end=end_date, freq="D")

    if df.empty:
        series = pd.Series(index=date_range, dtype=float)
    else:
        df["time_stamp"] = pd.to_datetime(df["time_stamp"])
        series = (
            df.groupby(df["time_stamp"].dt.normalize())["humidity"].mean().reindex(date_range)
        )

    values = [
        DailyValue(date=d.strftime("%Y-%m-%d"), value=(None if pd.isna(v) else float(v)))
        for d, v in series.items()
    ]

    return DataResponse(greenhouse_id=greenhouse_id, greenhouse_name=name, values=values)


@app.get("/data/temperature/{greenhouse_id}/range", response_model=RangeResponse)
def get_temperature_range(greenhouse_id: int, start: str, end: str):
    """Return all temperature readings between two ISO date/timestamp strings."""
    name = get_greenhouse_name(greenhouse_id)
    df = load_register_data_range(greenhouse_id, start, end)

    if df.empty:
        values = []
    else:
        df["time_stamp"] = pd.to_datetime(df["time_stamp"])
        values = [
            RangeItem(timestamp=ts.isoformat(), temperature=(None if pd.isna(t) else float(t)))
            for ts, t in zip(df["time_stamp"], df["temperature"])
        ]

    return RangeResponse(greenhouse_id=greenhouse_id, greenhouse_name=name, values=values)


@app.post("/reports", response_model=JobStatusResponse)
def create_report(payload: ReportRequestPayload):
    job_id = str(uuid.uuid4())
    jobs[job_id] = {"status": "queued", "message": None, "pdf": None}

    callback_url = f"{settings.analytics_base_url}/reports/{job_id}/complete"
    publish_report_request(job_id, payload.greenhouse_id, callback_url)

    return {"job_id": job_id, "status": "queued"}


@app.get("/reports/{job_id}", response_model=JobStatusResponse)
def get_report_status(job_id: str):
    job = jobs.get(job_id)
    if job is None:
        raise HTTPException(status_code=404, detail="Job not found")
    return {
        "job_id": job_id,
        "status": job["status"],
        "message": job.get("message"),
    }


@app.get("/reports/{job_id}/download")
def download_report(job_id: str):
    job = jobs.get(job_id)
    if job is None:
        raise HTTPException(status_code=404, detail="Job not found")
    if job["status"] != "ready" or not job.get("pdf"):
        raise HTTPException(status_code=409, detail="Report not ready")
    headers = {"Content-Disposition": f"attachment; filename=report_{job_id}.pdf"}
    return Response(content=job["pdf"], media_type="application/pdf", headers=headers)


@app.post("/reports/{job_id}/complete")
async def complete_report(job_id: str, request: Request):
    job = jobs.get(job_id)
    if job is None:
        raise HTTPException(status_code=404, detail="Job not found")

    content_type = request.headers.get("content-type", "")
    body = await request.body()

    if content_type.startswith("application/pdf"):
        job["status"] = "ready"
        job["pdf"] = body
        return {"job_id": job_id, "status": "ready"}

    try:
        data = await request.json()
    except Exception:
        data = {}

    message = data.get("message") or "Error generando reporte"
    job["status"] = "error"
    job["message"] = message
    return {"job_id": job_id, "status": "error", "message": message}


if __name__ == "__main__":
    import uvicorn

    uvicorn.run("main:app", host="0.0.0.0", port=8084)
