import customtkinter as ctk
from views.login_view import LoginView
from views.dashboard_view import DashboardView

ctk.set_appearance_mode("dark")
ctk.set_default_color_theme("green")

def start_dashboard(login_window):
    login_window.destroy()
    dashboard = DashboardView(on_logout=lambda: restart())
    dashboard.mainloop()

def restart():
    main()

def main():
    app = LoginView(on_login_success=start_dashboard)
    app.mainloop()

if __name__ == "__main__":
    main()