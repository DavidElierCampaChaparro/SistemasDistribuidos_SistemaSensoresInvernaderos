import io
from datetime import datetime
from typing import Dict, Optional

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from reportlab.lib.pagesizes import letter
from reportlab.lib.utils import ImageReader
from reportlab.pdfgen import canvas


def build_line_chart(series, title: str) -> io.BytesIO:
    fig, ax = plt.subplots(figsize=(6.5, 2.4), dpi=110)
    if series is None or series.dropna().empty:
        ax.text(0.5, 0.5, "Sin datos", ha="center", va="center")
        ax.set_axis_off()
    else:
        ax.plot(series.index, series.values, marker="o", linewidth=1.5)
        ax.set_title(title)
        ax.set_xlabel("Fecha")
        ax.set_ylabel("Temperatura")
        fig.autofmt_xdate(rotation=30, ha="right")
    fig.tight_layout()
    buffer = io.BytesIO()
    fig.savefig(buffer, format="png")
    plt.close(fig)
    buffer.seek(0)
    return buffer


def _fmt_value(value: Optional[float]) -> str:
    if value is None:
        return "N/A"
    try:
        return f"{value:.2f}"
    except Exception:
        return str(value)


def build_pdf_report(
    greenhouse_id: int,
    greenhouse_name: str,
    stats: Dict[str, Optional[float]],
    day_chart: io.BytesIO,
    night_chart: io.BytesIO,
) -> bytes:
    buffer = io.BytesIO()
    c = canvas.Canvas(buffer, pagesize=letter)
    width, height = letter
    y = height - 50

    c.setFont("Helvetica-Bold", 16)
    c.drawString(50, y, "Reporte del Invernadero")
    y -= 28

    c.setFont("Helvetica", 12)
    c.drawString(50, y, "Buenos dias")
    y -= 16
    c.drawString(50, y, f"Invernadero ID: {greenhouse_id}")
    y -= 16
    if greenhouse_name:
        c.drawString(50, y, f"Nombre: {greenhouse_name}")
        y -= 16
    c.drawString(50, y, f"Generado: {datetime.now().strftime('%Y-%m-%d %H:%M')}")
    y -= 24

    c.setFont("Helvetica-Bold", 12)
    c.drawString(50, y, "Resumen (ultimos 30 dias)")
    y -= 16

    c.setFont("Helvetica", 10)
    lines = [
        f"Temperatura promedio: {_fmt_value(stats.get('temp_avg'))}",
        f"Temperatura maximo: {_fmt_value(stats.get('temp_max'))}",
        f"Temperatura minimo: {_fmt_value(stats.get('temp_min'))}",
        f"Temperatura desviacion estandar: {_fmt_value(stats.get('temp_std'))}",
        f"Humedad promedio: {_fmt_value(stats.get('humidity_avg'))}",
    ]
    for line in lines:
        c.drawString(50, y, line)
        y -= 14

    y -= 10
    chart_width = 500
    chart_height = 180

    c.setFont("Helvetica-Bold", 11)
    c.drawString(50, y, "Temperatura de dia")
    y -= 10
    c.drawImage(
        ImageReader(day_chart),
        50,
        y - chart_height,
        width=chart_width,
        height=chart_height,
        preserveAspectRatio=True,
    )
    y -= chart_height + 24

    c.setFont("Helvetica-Bold", 11)
    c.drawString(50, y, "Temperatura de noche")
    y -= 10
    c.drawImage(
        ImageReader(night_chart),
        50,
        y - chart_height,
        width=chart_width,
        height=chart_height,
        preserveAspectRatio=True,
    )

    c.showPage()
    c.save()
    buffer.seek(0)
    return buffer.read()
