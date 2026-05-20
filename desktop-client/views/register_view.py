import customtkinter as ctk
from components.loader import run_with_loader
from api.auth import register
 
ACCENT = "#5a8a3c"
ACCENT_HOVER = "#4a7a2c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
 
 
class RegisterView(ctk.CTkFrame):
    def __init__(self, parent, on_back):
        super().__init__(parent, fg_color=BG)
        self.on_back = on_back
        self._build()
 
    def _build(self):
        ctk.CTkFrame(self, height=5, fg_color=ACCENT, corner_radius=0).pack(fill="x")
 
        card = ctk.CTkFrame(self, fg_color=BG_CARD, corner_radius=18,
                            border_width=1, border_color=BORDER)
        center = ctk.CTkFrame(self, fg_color="transparent")
        center.pack(expand=True)
        card = ctk.CTkFrame(center, fg_color=BG_CARD, corner_radius=18,
                            border_width=1, border_color=BORDER, width=440)
        card.pack(padx=20, pady=60)
        card.pack_propagate(False)
 
        header = ctk.CTkFrame(card, fg_color="transparent")
        header.pack(pady=(45, 28))
        ctk.CTkLabel(header, text="🌿", font=ctk.CTkFont(size=40)).pack()
        ctk.CTkLabel(header, text="Create Account",
                     font=ctk.CTkFont(size=26, weight="bold"), text_color=TEXT).pack(pady=(6, 2))
        ctk.CTkLabel(header, text="Join GreenWatch today",
                     font=ctk.CTkFont(size=13), text_color=TEXT_MUTED).pack()
 
        f = ctk.CTkFrame(card, fg_color="transparent")
        f.pack(padx=60, fill="x")
 
        row = ctk.CTkFrame(f, fg_color="transparent")
        row.pack(fill="x", pady=(0, 12))
        self.name = ctk.CTkEntry(row, placeholder_text="First name",
                                 height=46, corner_radius=8,
                                 border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.name.pack(side="left", expand=True, fill="x", padx=(0, 6))
        self.lastname = ctk.CTkEntry(row, placeholder_text="Last name",
                                     height=46, corner_radius=8,
                                     border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.lastname.pack(side="left", expand=True, fill="x", padx=(6, 0))
 
        self.email = ctk.CTkEntry(f, placeholder_text="Email address",
                                  height=46, corner_radius=8,
                                  border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.email.pack(fill="x", pady=(0, 12))
 
        self.password = ctk.CTkEntry(f, placeholder_text="Password",
                                     show="*", height=46, corner_radius=8,
                                     border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.password.pack(fill="x")
 
        self.error_label = ctk.CTkLabel(f, text="", text_color="#c0392b",
                                        font=ctk.CTkFont(size=12))
        self.error_label.pack(pady=(10, 0))
        self.success_label = ctk.CTkLabel(f, text="", text_color=ACCENT,
                                          font=ctk.CTkFont(size=12))
        self.success_label.pack(pady=(2, 0))
 
        b = ctk.CTkFrame(card, fg_color="transparent")
        b.pack(padx=60, fill="x", pady=(12, 50))
 
        ctk.CTkButton(b, text="Create Account", height=46, corner_radius=8,
                      fg_color=ACCENT, hover_color=ACCENT_HOVER,
                      text_color="white", font=ctk.CTkFont(size=14, weight="bold"),
                      command=self._submit).pack(fill="x", pady=(0, 10))
 
        ctk.CTkButton(b, text="← Back to Sign In", height=42, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, hover_color=BG,
                      command=self.on_back).pack(fill="x")
 
    def _submit(self):
        self.error_label.configure(text="")
        self.success_label.configure(text="")
        name = self.name.get().strip()
        lastname = self.lastname.get().strip()
        email = self.email.get().strip()
        password = self.password.get().strip()
        if not all([name, lastname, email, password]):
            self.error_label.configure(text="Please fill in all fields")
            return
 
        def task():
            return register(name, lastname, email, password)
 
        def done(result, error):
            if error:
                self.error_label.configure(text="Could not connect to server")
                return
            success, message = result
            if success:
                self.success_label.configure(text="✓ Account created — you can now sign in")
            else:
                self.error_label.configure(text=message or "Registration failed")
 
        run_with_loader(self, task, done, message="Creating account...")