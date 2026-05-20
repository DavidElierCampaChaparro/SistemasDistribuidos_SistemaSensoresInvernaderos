import customtkinter as ctk
from components.loader import run_with_loader
from api.auth import login
 
ACCENT = "#5a8a3c"
ACCENT_HOVER = "#4a7a2c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
 
 
class LoginView(ctk.CTkFrame):
    def __init__(self, parent, on_login_success, on_go_register):
        super().__init__(parent, fg_color=BG)
        self.on_login_success = on_login_success
        self.on_go_register = on_go_register
        self._build()
 
    def _build(self):
        ctk.CTkFrame(self, height=5, fg_color=ACCENT, corner_radius=0).pack(fill="x")

        # Contenedor centrado
        center = ctk.CTkFrame(self, fg_color="transparent")
        center.pack(expand=True)

        card = ctk.CTkFrame(center, fg_color=BG_CARD, corner_radius=18,
                            border_width=1, border_color=BORDER,
                            width=420)
        card.pack(padx=20, pady=60)
        card.pack_propagate(False)

        logo = ctk.CTkFrame(card, fg_color="transparent")
        logo.pack(pady=(50, 32))
        ctk.CTkLabel(logo, text="🌿", font=ctk.CTkFont(size=48)).pack()
        ctk.CTkLabel(logo, text="GreenWatch",
                    font=ctk.CTkFont(size=28, weight="bold"),
                    text_color=TEXT).pack(pady=(6, 2))
        ctk.CTkLabel(logo, text="Greenhouse Monitoring System",
                    font=ctk.CTkFont(size=13), text_color=TEXT_MUTED).pack()

        f = ctk.CTkFrame(card, fg_color="transparent")
        f.pack(padx=50, fill="x")

        self.email = ctk.CTkEntry(f, placeholder_text="Email address",
                                height=46, corner_radius=8,
                                border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.email.pack(fill="x", pady=(0, 12))
        self.email.bind("<Return>", lambda e: self._login())

        self.password = ctk.CTkEntry(f, placeholder_text="Password",
                                    show="*", height=46, corner_radius=8,
                                    border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.password.pack(fill="x")
        self.password.bind("<Return>", lambda e: self._login())

        self.error_label = ctk.CTkLabel(f, text="", text_color="#c0392b",
                                        font=ctk.CTkFont(size=12))
        self.error_label.pack(pady=(10, 0))

        b = ctk.CTkFrame(card, fg_color="transparent")
        b.pack(padx=50, fill="x", pady=(12, 50))

        ctk.CTkButton(b, text="Sign In", height=46, corner_radius=8,
                    fg_color=ACCENT, hover_color=ACCENT_HOVER,
                    text_color="white", font=ctk.CTkFont(size=14, weight="bold"),
                    command=self._login).pack(fill="x", pady=(0, 10))

        ctk.CTkButton(b, text="Create account", height=42, corner_radius=8,
                    fg_color="transparent", border_width=1, border_color=BORDER,
                    text_color=TEXT_MUTED, hover_color=BG,
                    command=self.on_go_register).pack(fill="x")
 
    def _login(self):
        self.error_label.configure(text="")
        email = self.email.get().strip()
        password = self.password.get().strip()
        if not email or not password:
            self.error_label.configure(text="Please fill in all fields")
            return
 
        def task():
            return login(email, password)
 
        def done(result, error):
            if error:
                self.error_label.configure(text="Could not connect to server")
                return
            success, msg = result
            if success:
                self.on_login_success()
            else:
                self.error_label.configure(text=msg or "Invalid credentials")
 
        run_with_loader(self, task, done, message="Signing in...")