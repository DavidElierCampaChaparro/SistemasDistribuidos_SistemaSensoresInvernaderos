import customtkinter as ctk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from datetime import datetime, timedelta
from components.loader import run_with_loader
from api.analytics import get_by_greenhouse, get_average
 
ACCENT = "#5a8a3c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
RED = "#c0392b"
 
 
class ChartsView(ctk.CTkToplevel):
    def __init__(self, parent, greenhouse):
        super().__init__(parent)
        self.greenhouse = greenhouse
        self.title(f"GreenWatch — Charts: {greenhouse['name']}")
        self.geometry("900x660")
        self.resizable(True, True)
        self.configure(fg_color=BG)
        self.grab_set()
        self._center()
        self._build()
        self._load_charts()
 
    def _center(self):
        self.update_idletasks()
        sw, sh = self.winfo_screenwidth(), self.winfo_screenheight()
        self.geometry(f"900x660+{(sw-900)//2}+{(sh-660)//2}")
 
    def _build(self):
        topbar = ctk.CTkFrame(self, height=60, fg_color=BG_CARD,
                              corner_radius=0, border_width=1, border_color=BORDER)
        topbar.pack(fill="x")
        topbar.pack_propagate(False)
 
        ctk.CTkLabel(topbar, text=f"📊  {self.greenhouse['name']} — Analytics",
                     font=ctk.CTkFont(size=17, weight="bold"),
                     text_color=TEXT).pack(side="left", padx=25, pady=16)
 
        filter_bar = ctk.CTkFrame(self, fg_color=BG_CARD,
                                  corner_radius=0, border_width=1, border_color=BORDER)
        filter_bar.pack(fill="x")
 
        inner = ctk.CTkFrame(filter_bar, fg_color="transparent")
        inner.pack(padx=25, pady=10, anchor="w")
 
        ctk.CTkLabel(inner, text="From", font=ctk.CTkFont(size=12),
                     text_color=TEXT_MUTED).pack(side="left", padx=(0, 5))
        self.from_entry = ctk.CTkEntry(inner, placeholder_text="YYYY-MM-DD",
                                       width=130, height=36, corner_radius=8,
                                       border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.from_entry.pack(side="left", padx=(0, 15))
        self.from_entry.insert(0, (datetime.now() - timedelta(days=7)).strftime("%Y-%m-%d"))
 
        ctk.CTkLabel(inner, text="To", font=ctk.CTkFont(size=12),
                     text_color=TEXT_MUTED).pack(side="left", padx=(0, 5))
        self.to_entry = ctk.CTkEntry(inner, placeholder_text="YYYY-MM-DD",
                                     width=130, height=36, corner_radius=8,
                                     border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.to_entry.pack(side="left", padx=(0, 15))
        self.to_entry.insert(0, datetime.now().strftime("%Y-%m-%d"))
 
        ctk.CTkButton(inner, text="Load", width=90, height=36, corner_radius=8,
                      fg_color=ACCENT, hover_color="#4a7a2c",
                      text_color="white", font=ctk.CTkFont(weight="bold"),
                      command=self._load_charts).pack(side="left")
 
        self.error_label = ctk.CTkLabel(inner, text="", text_color=RED,
                                        font=ctk.CTkFont(size=12))
        self.error_label.pack(side="left", padx=15)
 
        self.chart_frame = ctk.CTkFrame(self, fg_color="transparent")
        self.chart_frame.pack(fill="both", expand=True, padx=25, pady=15)
 
    def _load_charts(self):
        for w in self.chart_frame.winfo_children():
            w.destroy()
        self.error_label.configure(text="")
 
        from_dt = self.from_entry.get().strip() + "T00:00:00"
        to_dt = self.to_entry.get().strip() + "T23:59:59"
        gh_id = self.greenhouse["id"]
 
        def task():
            records = get_by_greenhouse(gh_id, from_dt, to_dt)
            avg = get_average(gh_id, from_dt, to_dt) if records else None
            return records, avg
 
        def done(result, error):
            if error:
                self.error_label.configure(text=f"Could not load data: {error}")
                return
 
            records, avg = result
 
            if not records:
                empty = ctk.CTkFrame(self.chart_frame, fg_color=BG_CARD,
                                     corner_radius=12, border_width=1, border_color=BORDER)
                empty.pack(expand=True, fill="both")
                ctk.CTkLabel(empty, text="No data available for selected range.",
                             text_color=TEXT_MUTED, font=ctk.CTkFont(size=14)).pack(expand=True)
                return
 
            timestamps = list(range(len(records)))
            temperatures = [r["temperature"] for r in records]
            humidities = [r["humidity"] for r in records]
 
            fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(9, 5), facecolor="#f5f0e8")
            fig.subplots_adjust(hspace=0.45)
 
            for ax, data, color, label, threshold_key, unit in [
                (ax1, temperatures, "#5a8a3c", "Temperature", "triggerTemperature", "°C"),
                (ax2, humidities, "#2980b9", "Humidity", "triggerHumidity", "%"),
            ]:
                ax.set_facecolor("#ffffff")
                ax.plot(timestamps, data, color=color, linewidth=2, zorder=3)
                ax.fill_between(timestamps, data, alpha=0.12, color=color)
                ax.axhline(y=self.greenhouse[threshold_key], color=color,
                           linestyle="--", alpha=0.6, linewidth=1.2,
                           label=f"Threshold: {self.greenhouse[threshold_key]}{unit}")
                ax.set_title(f"{label} ({unit})", color="#2c2416", fontsize=12, pad=8)
                ax.tick_params(colors="#8a7a6a", labelsize=9, bottom=False, labelbottom=False)
                ax.spines[:].set_color("#d4c8b8")
                ax.legend(facecolor="#ffffff", labelcolor="#8a7a6a",
                          fontsize=9, framealpha=0.9)
                ax.grid(True, color="#e8e0d0", linewidth=0.6, alpha=0.8)
 
            canvas = FigureCanvasTkAgg(fig, master=self.chart_frame)
            canvas.draw()
            canvas.get_tk_widget().pack(fill="both", expand=True)
 
            if avg:
                stats = ctk.CTkFrame(self.chart_frame, fg_color=BG_CARD,
                                     corner_radius=10, border_width=1, border_color=BORDER)
                stats.pack(fill="x", pady=(10, 0))
                si = ctk.CTkFrame(stats, fg_color="transparent")
                si.pack(pady=12)
                ctk.CTkLabel(si, text=f"📊  {len(records)} records",
                             font=ctk.CTkFont(size=12), text_color=TEXT_MUTED).pack(side="left", padx=20)
                ctk.CTkLabel(si, text=f"🌡  Avg Temp: {avg['temperature']:.1f}°C",
                             font=ctk.CTkFont(size=12, weight="bold"),
                             text_color="#5a8a3c").pack(side="left", padx=20)
                ctk.CTkLabel(si, text=f"💧  Avg Humidity: {avg['humidity']:.1f}%",
                             font=ctk.CTkFont(size=12, weight="bold"),
                             text_color="#2980b9").pack(side="left", padx=20)
 
        run_with_loader(self, task, done, message="Loading charts...")