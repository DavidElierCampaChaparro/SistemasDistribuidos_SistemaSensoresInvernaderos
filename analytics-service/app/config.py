from dataclasses import dataclass


@dataclass(frozen=True)
class Settings:
    analytics_base_url: str = "http://localhost:8084"
    rabbitmq_host: str = "localhost"
    rabbitmq_port: int = 5672
    rabbitmq_user: str = "guest"
    rabbitmq_password: str = "guest"
    report_queue: str = "analytics.reports.queue"
    mysql_host: str = "localhost"
    mysql_port: int = 3306
    mysql_user: str = "root"
    mysql_password: str = "root"
    greenhouse_db: str = "greenhouse_management_service"
    sensor_db: str = "sensor_management_service"


settings = Settings()
