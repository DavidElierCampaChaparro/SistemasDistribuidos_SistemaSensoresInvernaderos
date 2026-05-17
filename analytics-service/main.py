from typing import Optional
import uuid

from fastapi import FastAPI, HTTPException, Request, Response
from pydantic import BaseModel

from app.config import settings
from app.queue import publish_report_request

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
