import customtkinter as ctk
from api.sensor import get_by_greenhouse, delete as delete_sensor, create as create_sensor
 
ACCENT = "#5a8a3c"
ACCENT_HOVER = "#4a7a2c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
BG_SIDEBAR = "#ede8df"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
RED = "#c0392b"
 
FORMATS = ["BRAND_B_V2_JSON", "BRAND_A_X100_JSON", "BRAND_A_X100_BINARY", "BRAND_C_PRO_XML"]
 
 
class GreenhouseDetailView(ctk.CTk):
    def __init__(self, greenhouse, on_back):
        super().__init__()
        self.greenhouse = greenhouse
        self.on_back = on_back
        self.title(f"GreenWatch — {greenhouse['name']}")
        self.configure(fg_color=BG)
        self.after(0, lambda: self.state("zoomed"))
        self._build_ui()
        self._load_sensors()
 
    def _build_ui(self):
        # Topbar
        topbar = ctk.CTkFrame(self, height=64, fg_color=BG_CARD,
                              corner_radius=0, border_width=1, border_color=BORDER)
        topbar.pack(fill="x")
        topbar.pack_propagate(False)
 
        ctk.CTkButton(topbar, text="← Back", width=90, height=36,
                      corner_radius=8, fg_color="transparent",
                      border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, hover_color=BG,
                      command=self._back).pack(side="left", padx=20, pady=14)
 
        ctk.CTkLabel(topbar, text=self.greenhouse["name"],
                     font=ctk.CTkFont(size=20, weight="bold"),
                     text_color=TEXT).pack(side="left", padx=10, pady=18)
 
        ctk.CTkButton(topbar, text="📊 Charts", width=110, height=36,
                      corner_radius=8, fg_color="#dff0d0", hover_color="#cfe8bc",
                      text_color=ACCENT, font=ctk.CTkFont(weight="bold"),
                      command=self._open_charts).pack(side="right", padx=8, pady=14)
 
        ctk.CTkButton(topbar, text="✏ Edit", width=90, height=36,
                      corner_radius=8, fg_color="transparent",
                      border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, hover_color=BG,
                      command=self._edit_greenhouse).pack(side="right", padx=(0, 8), pady=14)
 
        # Info panel
        info_panel = ctk.CTkFrame(self, fg_color=BG_CARD,
                                  corner_radius=0, border_width=1, border_color=BORDER)
        info_panel.pack(fill="x")
 
        info_inner = ctk.CTkFrame(info_panel, fg_color="transparent")
        info_inner.pack(fill="x", padx=30, pady=16)
 
        self._stat(info_inner, "📍 Location", self.greenhouse["location"]).pack(side="left", padx=(0, 50))
        self._stat(info_inner, "🌡 Temp Threshold",
                   f"{self.greenhouse['triggerTemperature']}°C").pack(side="left", padx=(0, 50))
        self._stat(info_inner, "💧 Humidity Threshold",
                   f"{self.greenhouse['triggerHumidity']}%").pack(side="left")
 
        # Sensors header
        sensors_bar = ctk.CTkFrame(self, fg_color="transparent")
        sensors_bar.pack(fill="x", padx=30, pady=(22, 8))
 
        ctk.CTkLabel(sensors_bar, text="Sensors",
                     font=ctk.CTkFont(size=17, weight="bold"),
                     text_color=TEXT).pack(side="left")
 
        ctk.CTkButton(sensors_bar, text="+ Add Sensor", width=130, height=36,
                      corner_radius=8, fg_color=ACCENT, hover_color=ACCENT_HOVER,
                      text_color="white", font=ctk.CTkFont(weight="bold"),
                      command=self._add_sensor).pack(side="right")
 
        self.scroll_frame = ctk.CTkScrollableFrame(self, fg_color="transparent")
        self.scroll_frame.pack(fill="both", expand=True, padx=30, pady=(0, 20))
 
    def _stat(self, parent, label, value):
        f = ctk.CTkFrame(parent, fg_color="transparent")
        ctk.CTkLabel(f, text=label, font=ctk.CTkFont(size=11),
                     text_color=TEXT_MUTED).pack(anchor="w")
        ctk.CTkLabel(f, text=value, font=ctk.CTkFont(size=15, weight="bold"),
                     text_color=TEXT).pack(anchor="w")
        return f
 
    def _load_sensors(self):
        for w in self.scroll_frame.winfo_children():
            w.destroy()
        try:
            sensors = get_by_greenhouse(self.greenhouse["id"])
            if not sensors:
                empty = ctk.CTkFrame(self.scroll_frame, fg_color=BG_CARD,
                                     corner_radius=12, border_width=1, border_color=BORDER)
                empty.pack(fill="x", pady=10)
                ctk.CTkLabel(empty, text="No sensors registered yet.",
                             text_color=TEXT_MUTED).pack(pady=30)
                return
            for s in sensors:
                self._add_sensor_card(s)
        except Exception:
            ctk.CTkLabel(self.scroll_frame, text="Could not load sensors.",
                         text_color=RED).pack(pady=20)
 
    def _add_sensor_card(self, sensor):
        card = ctk.CTkFrame(self.scroll_frame, fg_color=BG_CARD,
                            corner_radius=10, border_width=1, border_color=BORDER)
        card.pack(fill="x", pady=5)
 
        left = ctk.CTkFrame(card, fg_color="transparent")
        left.pack(side="left", fill="both", expand=True, padx=20, pady=14)
 
        ctk.CTkLabel(left, text=f"📡  {sensor['serialNumber']}",
                     font=ctk.CTkFont(size=14, weight="bold"),
                     text_color=TEXT).pack(anchor="w")
        ctk.CTkLabel(left, text=f"Format: {sensor['format']}",
                     font=ctk.CTkFont(size=12), text_color=TEXT_MUTED).pack(anchor="w", pady=(2, 0))
 
        ctk.CTkButton(card, text="Delete", width=90, height=34, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color="#e8c0bc",
                      text_color=RED, hover_color="#fdf0ee",
                      command=lambda s=sensor: self._delete_sensor(s)).pack(
                      side="right", padx=20, pady=14)
 
    def _add_sensor(self):
        dialog = ctk.CTkToplevel(self)
        dialog.title("Add Sensor")
        dialog.geometry("420x300")
        dialog.resizable(False, False)
        dialog.configure(fg_color=BG)
        dialog.grab_set()
        dialog.update_idletasks()
        sw, sh = dialog.winfo_screenwidth(), dialog.winfo_screenheight()
        dialog.geometry(f"420x300+{(sw-420)//2}+{(sh-300)//2}")
 
        inner = ctk.CTkFrame(dialog, fg_color=BG_CARD, corner_radius=14,
                             border_width=1, border_color=BORDER)
        inner.pack(expand=True, fill="both", padx=20, pady=20)
 
        ctk.CTkLabel(inner, text="Add Sensor",
                     font=ctk.CTkFont(size=18, weight="bold"),
                     text_color=TEXT).pack(pady=(22, 18))
 
        serial_entry = ctk.CTkEntry(inner, placeholder_text="Serial number",
                                    height=42, corner_radius=8,
                                    border_color=BORDER, fg_color=BG,
                                    text_color=TEXT, width=300)
        serial_entry.pack(pady=(0, 10))
 
        ctk.CTkLabel(inner, text="Format", font=ctk.CTkFont(size=12),
                     text_color=TEXT_MUTED).pack()
        format_var = ctk.StringVar(value=FORMATS[0])
        format_menu = ctk.CTkOptionMenu(inner, values=FORMATS, variable=format_var,
                                        width=300, height=42, corner_radius=8,
                                        fg_color=BG, button_color=BORDER,
                                        text_color=TEXT)
        format_menu.pack(pady=(4, 8))
 
        error_label = ctk.CTkLabel(inner, text="", text_color=RED,
                                   font=ctk.CTkFont(size=12))
        error_label.pack()
 
        def save():
            serial = serial_entry.get().strip()
            if not serial:
                error_label.configure(text="Serial number is required")
                return
            try:
                create_sensor(serial, self.greenhouse["id"], format_var.get())
                dialog.destroy()
                self._load_sensors()
            except Exception:
                error_label.configure(text="Could not add sensor")
 
        btn_row = ctk.CTkFrame(inner, fg_color="transparent")
        btn_row.pack(fill="x", padx=20, pady=(6, 18))
 
        ctk.CTkButton(btn_row, text="Save", height=40, corner_radius=8,
                      fg_color=ACCENT, hover_color=ACCENT_HOVER,
                      text_color="white", font=ctk.CTkFont(weight="bold"),
                      command=save).pack(side="left", expand=True, fill="x", padx=(0, 5))
        ctk.CTkButton(btn_row, text="Cancel", height=40, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, command=dialog.destroy).pack(
                      side="left", expand=True, fill="x", padx=(5, 0))
 
    def _delete_sensor(self, sensor):
        from tkinter import messagebox
        if messagebox.askyesno("Confirm", f"Delete sensor '{sensor['serialNumber']}'?"):
            try:
                delete_sensor(sensor["id"])
                self._load_sensors()
            except Exception:
                messagebox.showerror("Error", "Could not delete sensor")
 
    def _edit_greenhouse(self):
        from views.greenhouse_form_view import GreenhouseFormView
        GreenhouseFormView(self, greenhouse=self.greenhouse,
                           on_save=self._refresh_greenhouse)
 
    def _refresh_greenhouse(self):
        from api.greenhouse import get_by_id
        self.greenhouse = get_by_id(self.greenhouse["id"])
        self.destroy()
        GreenhouseDetailView(self.greenhouse, self.on_back).mainloop()
 
    def _open_charts(self):
        from views.charts_view import ChartsView
        ChartsView(self, self.greenhouse)
 
    def _back(self):
        self.destroy()
        self.on_back()