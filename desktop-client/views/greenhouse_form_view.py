import re
import customtkinter as ctk
from api.greenhouse import create, update
from api.auth import get_user_id
 
ACCENT = "#5a8a3c"
ACCENT_HOVER = "#4a7a2c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
RED = "#c0392b"
WIDTH = 480
HEIGHT = 540
 
 
class GreenhouseFormView(ctk.CTkToplevel):
    def __init__(self, parent, on_save, greenhouse=None):
        super().__init__(parent)
        self.owner_id = get_user_id()
        self.on_save = on_save
        self.greenhouse = greenhouse
        self.title("Edit Greenhouse" if greenhouse else "Add Greenhouse")
        self.geometry(f"{WIDTH}x{HEIGHT}")
        self.resizable(False, False)
        self.configure(fg_color=BG)
        self.grab_set()
        self._center()
        self._build_ui()
 
    def _center(self):
        self.update_idletasks()
        sw = self.winfo_screenwidth()
        sh = self.winfo_screenheight()
        x = (sw - WIDTH) // 2
        y = (sh - HEIGHT) // 2
        self.geometry(f"{WIDTH}x{HEIGHT}+{x}+{y}")
 
    def _build_ui(self):
        card = ctk.CTkFrame(self, fg_color=BG_CARD, corner_radius=16,
                            border_width=1, border_color=BORDER)
        card.pack(expand=True, fill="both", padx=25, pady=25)
 
        ctk.CTkLabel(card,
                     text="Edit Greenhouse" if self.greenhouse else "New Greenhouse",
                     font=ctk.CTkFont(size=20, weight="bold"),
                     text_color=TEXT).pack(pady=(28, 22))
 
        f = ctk.CTkFrame(card, fg_color="transparent")
        f.pack(padx=25, fill="x")
 
        self.name = self._field(f, "Greenhouse name")
        self.location = self._field(f, "Location")
 
        row = ctk.CTkFrame(f, fg_color="transparent")
        row.pack(fill="x", pady=(0, 10))
        self.trigger_temp = ctk.CTkEntry(row, placeholder_text="Temp threshold (°C)",
                                         height=44, corner_radius=8,
                                         border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.trigger_temp.pack(side="left", expand=True, fill="x", padx=(0, 6))
        self.trigger_humidity = ctk.CTkEntry(row, placeholder_text="Humidity (%)",
                                             height=44, corner_radius=8,
                                             border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.trigger_humidity.pack(side="left", expand=True, fill="x", padx=(6, 0))
 
        self.notification_email = self._field(f, "Notification email")
 
        if self.greenhouse:
            self.name.insert(0, self.greenhouse["name"])
            self.location.insert(0, self.greenhouse["location"])
            self.trigger_temp.insert(0, str(self.greenhouse["triggerTemperature"]))
            self.trigger_humidity.insert(0, str(self.greenhouse["triggerHumidity"]))
            if self.greenhouse.get("notificationEmail"):
                self.notification_email.insert(0, self.greenhouse["notificationEmail"])
 
        self.error_label = ctk.CTkLabel(f, text="", text_color=RED,
                                        font=ctk.CTkFont(size=12))
        self.error_label.pack(pady=(4, 0))
 
        b = ctk.CTkFrame(card, fg_color="transparent")
        b.pack(padx=25, fill="x", pady=(10, 25))
 
        ctk.CTkButton(b, text="Save", height=44, corner_radius=8,
                      fg_color=ACCENT, hover_color=ACCENT_HOVER,
                      text_color="white", font=ctk.CTkFont(size=14, weight="bold"),
                      command=self._save).pack(fill="x", pady=(0, 8))
 
        ctk.CTkButton(b, text="Cancel", height=40, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, hover_color=BG,
                      command=self.destroy).pack(fill="x")
 
    def _field(self, parent, placeholder):
        entry = ctk.CTkEntry(parent, placeholder_text=placeholder,
                             height=44, corner_radius=8,
                             border_color=BORDER, fg_color=BG, text_color=TEXT)
        entry.pack(fill="x", pady=(0, 10))
        return entry
 
    def _save(self):
        try:
            name = self.name.get().strip()
            location = self.location.get().strip()
            notification_email = self.notification_email.get().strip()
 
            if not all([name, location]):
                self.error_label.configure(text="Name and location are required")
                return
 
            trigger_temp = float(self.trigger_temp.get().strip())
            trigger_humidity = float(self.trigger_humidity.get().strip())
 
            if notification_email and not re.match(r"^[\w\.-]+@[\w\.-]+\.\w+$", notification_email):
                self.error_label.configure(text="Invalid email format")
                return
 
            if self.greenhouse:
                update(self.greenhouse["id"], name, location,
                       trigger_temp, trigger_humidity, notification_email)
            else:
                create(name, location, self.owner_id,
                       trigger_temp, trigger_humidity, notification_email)
 
            self.on_save()
            self.destroy()
        except ValueError:
            self.error_label.configure(text="Temperature and humidity must be numbers")
        except Exception:
            self.error_label.configure(text="Could not save greenhouse")