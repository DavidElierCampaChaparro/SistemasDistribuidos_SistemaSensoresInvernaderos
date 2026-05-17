import customtkinter as ctk
from api.auth import register
 
ACCENT = "#5a8a3c"
ACCENT_HOVER = "#4a7a2c"
BG = "#f5f0e8"
BG_CARD = "#ffffff"
TEXT = "#2c2416"
TEXT_MUTED = "#8a7a6a"
BORDER = "#d4c8b8"
WIDTH = 460
HEIGHT = 600
 
 
class RegisterView(ctk.CTkToplevel):
    def __init__(self, on_back):
        super().__init__()
        self.on_back = on_back
        self.title("GreenWatch — Create Account")
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
 
        header = ctk.CTkFrame(card, fg_color="transparent")
        header.pack(pady=(35, 25))
        ctk.CTkLabel(header, text="🌿", font=ctk.CTkFont(size=36)).pack()
        ctk.CTkLabel(header, text="Create Account",
                     font=ctk.CTkFont(size=24, weight="bold"), text_color=TEXT).pack(pady=(6, 2))
        ctk.CTkLabel(header, text="Join GreenWatch today",
                     font=ctk.CTkFont(size=12), text_color=TEXT_MUTED).pack()
 
        f = ctk.CTkFrame(card, fg_color="transparent")
        f.pack(padx=30, fill="x")
 
        row = ctk.CTkFrame(f, fg_color="transparent")
        row.pack(fill="x", pady=(0, 10))
        self.name = ctk.CTkEntry(row, placeholder_text="First name",
                                 height=44, corner_radius=8,
                                 border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.name.pack(side="left", expand=True, fill="x", padx=(0, 5))
        self.lastname = ctk.CTkEntry(row, placeholder_text="Last name",
                                     height=44, corner_radius=8,
                                     border_color=BORDER, fg_color=BG, text_color=TEXT)
        self.lastname.pack(side="left", expand=True, fill="x", padx=(5, 0))
 
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
        self.success_label = ctk.CTkLabel(f, text="", text_color=ACCENT,
                                          font=ctk.CTkFont(size=12))
        self.success_label.pack(pady=(2, 0))
 
        b = ctk.CTkFrame(card, fg_color="transparent")
        b.pack(padx=30, fill="x", pady=(10, 30))
 
        ctk.CTkButton(b, text="Create Account", height=44, corner_radius=8,
                      fg_color=ACCENT, hover_color=ACCENT_HOVER,
                      text_color="white", font=ctk.CTkFont(size=14, weight="bold"),
                      command=self._register).pack(fill="x", pady=(0, 8))
 
        ctk.CTkButton(b, text="← Back to Sign In", height=40, corner_radius=8,
                      fg_color="transparent", border_width=1, border_color=BORDER,
                      text_color=TEXT_MUTED, hover_color=BG,
                      command=self._back).pack(fill="x")
 
    def _register(self):
        name = self.name.get().strip()
        lastname = self.lastname.get().strip()
        email = self.email.get().strip()
        password = self.password.get().strip()
        if not all([name, lastname, email, password]):
            self.error_label.configure(text="Please fill in all fields")
            return
        try:
            success, message = register(name, lastname, email, password)
            if success:
                self.success_label.configure(text="✓ Account created — you can now sign in")
                self.error_label.configure(text="")
            else:
                self.error_label.configure(text=message or "Registration failed")
        except Exception:
            self.error_label.configure(text="Could not connect to server")
 
    def _back(self):
        self.on_back()
        self.destroy()
 