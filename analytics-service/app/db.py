from typing import Optional

import pandas as pd
from sqlalchemy import create_engine, text

from .config import settings


def _build_url(db_name: str) -> str:
    return (
        "mysql+pymysql://"
        f"{settings.mysql_user}:{settings.mysql_password}"
        f"@{settings.mysql_host}:{settings.mysql_port}/{db_name}"
    )


greenhouse_engine = create_engine(_build_url(settings.greenhouse_db), pool_pre_ping=True)
sensor_engine = create_engine(_build_url(settings.sensor_db), pool_pre_ping=True)


def get_greenhouse_name(greenhouse_id: int) -> Optional[str]:
    sql = text("SELECT name FROM greenhouse WHERE id = :gid")
    with greenhouse_engine.connect() as conn:
        row = conn.execute(sql, {"gid": greenhouse_id}).first()
    return row[0] if row else None


def load_register_data(greenhouse_id: int, days: int = 30) -> pd.DataFrame:
    sql = f"""
        SELECT r.time_stamp, r.temperature, r.humidity
        FROM register r
        JOIN sensor s ON r.sensor_id = s.id
        WHERE s.greenhouse_id = %(gid)s
          AND r.time_stamp >= (NOW() - INTERVAL {int(days)} DAY)
        ORDER BY r.time_stamp ASC
    """
    with sensor_engine.connect() as conn:
        return pd.read_sql(sql, conn, params={"gid": greenhouse_id})
