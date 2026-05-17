import json

import pika

from .config import settings


def publish_report_request(job_id: str, greenhouse_id: int, callback_url: str) -> None:
    credentials = pika.PlainCredentials(settings.rabbitmq_user, settings.rabbitmq_password)
    params = pika.ConnectionParameters(
        host=settings.rabbitmq_host,
        port=settings.rabbitmq_port,
        credentials=credentials,
    )

    connection = pika.BlockingConnection(params)
    channel = connection.channel()
    channel.queue_declare(queue=settings.report_queue, durable=True)

    body = json.dumps(
        {
            "job_id": job_id,
            "greenhouse_id": greenhouse_id,
            "callback_url": callback_url,
        }
    ).encode("utf-8")

    channel.basic_publish(
        exchange="",
        routing_key=settings.report_queue,
        body=body,
        properties=pika.BasicProperties(delivery_mode=2),
    )
    connection.close()
