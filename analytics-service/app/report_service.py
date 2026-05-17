from typing import Dict, Optional, Tuple

import numpy as np
import pandas as pd
from scipy import stats

from .db import get_greenhouse_name, load_register_data
from .pdf_builder import build_line_chart, build_pdf_report


def generate_report_pdf(greenhouse_id: int) -> bytes:
    greenhouse_name = get_greenhouse_name(greenhouse_id) or "Desconocido"
    df = load_register_data(greenhouse_id, days=30)

    if not df.empty:
        df["time_stamp"] = pd.to_datetime(df["time_stamp"])
        df["date"] = df["time_stamp"].dt.normalize()
        df["hour"] = df["time_stamp"].dt.hour
        df["period"] = np.where(
            (df["hour"] >= 6) & (df["hour"] < 18), "day", "night"
        )

    stats_block = compute_stats(df)
    day_series, night_series = build_daily_series(df)
    day_chart = build_line_chart(day_series, "Temperatura de dia (ultimos 30 dias)")
    night_chart = build_line_chart(
        night_series, "Temperatura de noche (ultimos 30 dias)"
    )

    return build_pdf_report(
        greenhouse_id,
        greenhouse_name,
        stats_block,
        day_chart,
        night_chart,
    )


def compute_stats(df: pd.DataFrame) -> Dict[str, Optional[float]]:
    if df.empty:
        return {
            "temp_avg": None,
            "temp_max": None,
            "temp_min": None,
            "temp_std": None,
            "humidity_avg": None,
        }

    temps = df["temperature"].to_numpy(dtype=float)
    humidity = df["humidity"].to_numpy(dtype=float)

    return {
        "temp_avg": float(stats.tmean(temps)),
        "temp_max": float(np.nanmax(temps)),
        "temp_min": float(np.nanmin(temps)),
        "temp_std": float(stats.tstd(temps)),
        "humidity_avg": float(stats.tmean(humidity)),
    }


def build_daily_series(df: pd.DataFrame) -> Tuple[pd.Series, pd.Series]:
    if df.empty:
        empty = pd.Series(dtype=float)
        return empty, empty

    end_date = pd.Timestamp.now().normalize()
    start_date = end_date - pd.Timedelta(days=29)
    date_range = pd.date_range(start=start_date, end=end_date, freq="D")

    day_series = (
        df[df["period"] == "day"]
        .groupby("date")["temperature"]
        .mean()
        .reindex(date_range)
    )
    night_series = (
        df[df["period"] == "night"]
        .groupby("date")["temperature"]
        .mean()
        .reindex(date_range)
    )

    return day_series, night_series
