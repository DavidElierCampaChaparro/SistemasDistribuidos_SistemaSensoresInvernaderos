import customtkinter as ctk
from components.loader import run_with_loader
from components.toast import show_toast
from api.greenhouse import get_all, delete
from api.auth import get_user_email
 
ACCENT = "#5a8a3c"
ACCENT_HOVER = "#4a7a2c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
BG_SIDEBAR = "#ede8df"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
RED = "#c0392b"
 
 
class DashboardView(ctk.CTkFrame):
    def __init__(self, parent, on_logout, on_open_detail):
        super().__init__(parent, fg_color=BG)
        self.on_logout = on_logout
        self.on_open_detail = on_open_detail
        self._build()
        self._load_greenhouses()
 
    def _build(self):
        # Sidebar
        sidebar = ctk.CTkFrame(self, width=230, fg_color=BG_SIDEBAR,
                               corner_radius=0, border_width=1, border_color=BORDER)
        sidebar.pack(side="left", fill="y")
        sidebar.pack_propagate(False)
 
        ctk.CTkLabel(sidebar, text="🌿", font=ctk.CTkFont(size=36)).pack(pady=(35, 4))
        ctk.CTkLabel(sidebar, text="GreenWatch",
                     font=ctk.CTkFont(size=17, weight="bold"), text_color=TEXT).pack()
        ctk.CTkLabel(sidebar, text="Monitoring System",
                     font=ctk.CTkFont(size=11), text_color=TEXT_MUTED).pack(pady=(0, 25))
 
        ctk.CTkFrame(sidebar, height=1, fg_color=BORDER).pack(fill="x", padx=18)
 
        ctk.CTkButton(sidebar, text="🏠  My Greenhouses", anchor="w",
                      fg_color="#dff0d0", hover_color="#cfe8bc",
                      text_color=ACCENT, height=40, corner_radius=8,
                      font=ctk.CTkFont(size=13, weight="bold")).pack(
                      fill="x", padx=15, pady=(20, 5))
 
        ctk.CTkFrame(sidebar, fg_color="transparent").pack(expand=True)
        ctk.CTkFrame(sidebar, height=1, fg_color=BORDER).pack(fill="x", padx=18)
 
        # Edit profile button
        ctk.CTkButton(sidebar, text="👤  Edit Profile", anchor="w",
                      fg_color="transparent", hover_color="#f0e0d0",
                      text_color=TEXT_MUTED, height=40, corner_radius=8,
                      command=self._edit_profile).pack(fill="x", padx=15, pady=(10, 0))
 
        ctk.CTkButton(sidebar, text="⏻  Sign Out", anchor="w",
                      fg_color="transparent", hover_color="#f0e0d0",
                      text_color=TEXT_MUTED, height=40, corner_radius=8,
                      command=self.on_logout).pack(fill="x", padx=15, pady=(5, 15))
 
        # Main area
        main = ctk.CTkFrame(self, fg_color=BG, corner_radius=0)
        main.pack(side="left", fill="both", expand=True)
 
        # Topbar
        topbar = ctk.CTkFrame(main, height=64, fg_color=BG_CARD,
                              corner_radius=0, border_width=1, border_color=BORDER)
        topbar.pack(fill="x")
        topbar.pack_propagate(False)
 
        ctk.CTkLabel(topbar, text="My Greenhouses",
                     font=ctk.CTkFont(size=20, weight="bold"),
                     text_color=TEXT).pack(side="left", padx=30, pady=18)
 
        ctk.CTkButton(topbar, text="+ Add Greenhouse", height=36,
                      corner_radius=8, fg_color=ACCENT, hover_color=ACCENT_HOVER,
                      text_color="white", font=ctk.CTkFont(weight="bold"),
                      command=self._add_greenhouse).pack(side="right", padx=25, pady=14)
 
        # Content scroll area
        content = ctk.CTkFrame(main, fg_color="transparent")
        content.pack(fill="both", expand=True, padx=30, pady=25)
 
        self.scroll_frame = ctk.CTkScrollableFrame(content, fg_color="transparent")
        self.scroll_frame.pack(fill="both", expand=True)
 
    def _load_greenhouses(self):
        for w in self.scroll_frame.winfo_children():
            w.destroy()
 
        def task():
            return get_all()
 
        def done(result, error):
            if error:
                ctk.CTkLabel(self.scroll_frame, text="Could not load greenhouses.",
                             text_color=RED).pack(pady=20)
                return
            greenhouses = result
            if not greenhouses:
                empty = ctk.CTkFrame(self.scroll_frame, fg_color=BG_CARD,
                                     corner_radius=12, border_width=1, border_color=BORDER)
                empty.pack(fill="x", pady=20, padx=5)
                ctk.CTkLabel(empty, text="🌱  No greenhouses registered yet",
                             font=ctk.CTkFont(size=15), text_color=TEXT_MUTED).pack(pady=50)
                return
            for gh in greenhouses:
                self._add_card(gh)
 
        run_with_loader(self, task, done, message="Loading greenhouses...")
 
    def _add_card(self, gh):
        card = ctk.CTkFrame(self.scroll_frame, fg_color=BG_CARD,
                            corner_radius=12, border_width=1, border_color=BORDER)
        card.pack(fill="x", pady=6, padx=5)
 
        left = ctk.CTkFrame(card, fg_color="transparent")
        left.pack(side="left", fill="both", expand=True, padx=20, pady=16)
 
        ctk.CTkLabel(left, text=gh["name"],
                     font=ctk.CTkFont(size=16, weight="bold"), text_color=TEXT).pack(anchor="w")
 
        meta = ctk.CTkFrame(left, fg_color="transparent")
        meta.pack(anchor="w", pady=(5, 0))
        ctk.CTkLabel(meta, text=f"📍 {gh['location']}",
                     font=ctk.CTkFont(size=12), text_color=TEXT_MUTED).pack(side="left", padx=(0, 18))
        ctk.CTkLabel(meta, text=f"🌡 {gh['triggerTemperature']}°C",
                     font=ctk.CTkFont(size=12), text_color=TEXT_MUTED).pack(side="left", padx=(0, 18))
        ctk.CTkLabel(meta, text=f"💧 {gh['triggerHumidity']}%",
                     font=ctk.CTkFont(size=12), text_color=TEXT_MUTED).pack(side="left")
 
        right = ctk.CTkFrame(card, fg_color="transparent")
        right.pack(side="right", padx=20, pady=16)
 
        ctk.CTkButton(right, text="View", width=90, height=36, corner_radius=8,
                      fg_color="#dff0d0", hover_color="#cfe8bc",
                      text_color=ACCENT, font=ctk.CTkFont(weight="bold"),
                      command=lambda g=gh: self.on_open_detail(g)).pack(side="left", padx=(0, 8))
 
        ctk.CTkButton(right, text="Edit", width=80, height=36, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, hover_color=BG,
                      command=lambda g=gh: self._edit(g)).pack(side="left", padx=(0, 8))
 
        ctk.CTkButton(right, text="Delete", width=90, height=36, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color="#e8c0bc",
                      text_color=RED, hover_color="#fdf0ee",
                      command=lambda g=gh: self._delete(g)).pack(side="left")
 
    def _add_greenhouse(self):
        from views.greenhouse_form_view import GreenhouseFormView
        GreenhouseFormView(self, on_save=self._load_greenhouses)
 
    def _edit(self, gh):
        from views.greenhouse_form_view import GreenhouseFormView
        GreenhouseFormView(self, greenhouse=gh, on_save=self._load_greenhouses)
 
    def _delete(self, gh):
        from tkinter import messagebox
        if not messagebox.askyesno("Confirm Delete", f"Delete '{gh['name']}'?"):
            return
 
        def task():
            return delete(gh["id"])
 
        def done(result, error):
            if error or not result:
                show_toast(self, "Could not delete greenhouse", kind="error")
                return
            show_toast(self, f"'{gh['name']}' deleted", kind="success")
            self._load_greenhouses()
 
        run_with_loader(self, task, done, message="Deleting...")
 
    def _edit_profile(self):
        from views.profile_view import ProfileView
        ProfileView(self, on_saved=lambda: show_toast(self, "Profile updated!", kind="success"))