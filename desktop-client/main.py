import customtkinter as ctk
from api.auth import logout
 
ctk.set_appearance_mode("light")
ctk.set_default_color_theme("green")
 
 
class App(ctk.CTk):
    """
    Single persistent root window.
    Views are CTkFrames swapped in/out — no window is ever destroyed
    and recreated, so there is no black-flash between screens.
    """
 
    def __init__(self):
        super().__init__()
        self.title("GreenWatch")
        self.configure(fg_color="#f5f0e8")
        self.after(0, lambda: self.state("zoomed"))
        self._current_frame = None
        self._show_login()
 
    # ── Navigation helpers ──────────────────────────────────────────────────
 
    def _swap(self, frame: ctk.CTkFrame):
        """Replace the current view with frame (already constructed)."""
        if self._current_frame is not None:
            self._current_frame.pack_forget()
            self._current_frame.destroy()
        self._current_frame = frame
        frame.pack(expand=True, fill="both")
 
    # ── Screens ─────────────────────────────────────────────────────────────
 
    def _show_login(self):
        from views.login_view import LoginView
        frame = LoginView(
            parent=self,
            on_login_success=self._show_dashboard,
            on_go_register=self._show_register
        )
        self._swap(frame)
 
    def _show_register(self):
        from views.register_view import RegisterView
        frame = RegisterView(
            parent=self,
            on_back=self._show_login
        )
        self._swap(frame)
 
    def _show_dashboard(self):
        from views.dashboard_view import DashboardView
        frame = DashboardView(
            parent=self,
            on_logout=self._logout,
            on_open_detail=self._show_detail
        )
        self._swap(frame)
 
    def _show_detail(self, greenhouse):
        from views.greenhouse_detail_view import GreenhouseDetailView
        frame = GreenhouseDetailView(
            parent=self,
            greenhouse=greenhouse,
            on_back=self._show_dashboard
        )
        self._swap(frame)
 
    def _logout(self):
        logout()
        self._show_login()
 
 
if __name__ == "__main__":
    App().mainloop()
 






























