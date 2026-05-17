import customtkinter as ctk
from api.auth import login
 
ctk.set_appearance_mode("light")
ctk.set_default_color_theme("green")
 
ACCENT = "#5a8a3c"
ACCENT_HOVER = "#4a7a2c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
WIDTH = 460
HEIGHT = 560
 
 
class LoginView(ctk.CTk):
    def __init__(self, on_login_success):
        super().__init__()
        self.on_login_success = on_login_success
        self.title("GreenWatch — Sign In")
        self.geometry(f"{WIDTH}x{HEIGHT}")
        self.resizable(False, False)
        self.configure(fg_color=BG)
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
        ctk.CTkFrame(self, height=5, fg_color=ACCENT, corner_radius=0).pack(fill="x")
 
        card = ctk.CTkFrame(self, fg_color=BG_CARD, corner_radius=18,
                            border_width=1, border_color=BORDER)
        card.pack(expand=True, fill="both", padx=35, pady=35)
 
        logo = ctk.CTkFrame(card, fg_color="transparent")
        logo.pack(pady=(40, 28))
        ctk.CTkLabel(logo, text="🌿", font=ctk.CTkFont(size=44)).pack()
        ctk.CTkLabel(logo, text="GreenWatch",
                     font=ctk.CTkFont(size=26, weight="bold"),
                     text_color=TEXT).pack(pady=(6, 2))
        ctk.CTkLabel(logo, text="Greenhouse Monitoring System",
                     font=ctk.CTkFont(size=12), text_color=TEXT_MUTED).pack()
 
        f = ctk.CTkFrame(card, fg_color="transparent")
        f.pack(padx=30, fill="x")
 
        self.email = ctk.CTkEntry(f, placeholder_text="Email address",
                                  height=44, corner_radius=8,
                                  border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.email.pack(fill="x", pady=(0, 10))
 
        self.password = ctk.CTkEntry(f, placeholder_text="Password",
                                     show="*", height=44, corner_radius=8,
                                     border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.password.pack(fill="x")
 
        self.error_label = ctk.CTkLabel(f, text="", text_color="#c0392b",
                                        font=ctk.CTkFont(size=12))
        self.error_label.pack(pady=(8, 0))
 
        b = ctk.CTkFrame(card, fg_color="transparent")
        b.pack(padx=30, fill="x", pady=(10, 35))
 
        ctk.CTkButton(b, text="Sign In", height=44, corner_radius=8,
                      fg_color=ACCENT, hover_color=ACCENT_HOVER,
                      text_color="white", font=ctk.CTkFont(size=14, weight="bold"),
                      command=self._login).pack(fill="x", pady=(0, 8))
 
        ctk.CTkButton(b, text="Create account", height=40, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, hover_color=BG,
                      command=self._go_register).pack(fill="x")
 
    def _login(self):
        email = self.email.get().strip()
        password = self.password.get().strip()
        if not email or not password:
            self.error_label.configure(text="Please fill in all fields")
            return
        try:
            success, result = login(email, password)
            if success:
                self.on_login_success(self)
            else:
                self.error_label.configure(text=result or "Invalid credentials")
        except Exception:
            self.error_label.configure(text="Could not connect to server")
 
    def _go_register(self):
        from views.register_view import RegisterView
        self.withdraw()
        RegisterView(on_back=lambda: self.deiconify()).mainloop()