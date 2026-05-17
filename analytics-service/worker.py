import json
import logging
import time
import urllib.request

import pika

from app.config import settings
from app.report_service import generate_report_pdf

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("analytics-worker")

MAX_RETRIES = 3
RETRY_DELAY_SECONDS = 2


# Send the PDF to the callback URL with basic retries.
def post_pdf(callback_url: str, pdf_bytes: bytes) -> None:
    request = urllib.request.Request(
        callback_url,
        data=pdf_bytes,
        headers={"Content-Type": "application/pdf"},
        method="POST",
    )
    for attempt in range(1, MAX_RETRIES + 1):
        try:
            with urllib.request.urlopen(request, timeout=30) as response:
                response.read()
            return
        except Exception as exc:
            logger.error("post_pdf failed (attempt %s/%s): %s", attempt, MAX_RETRIES, exc)
            if attempt < MAX_RETRIES:
                time.sleep(RETRY_DELAY_SECONDS)
    raise RuntimeError("post_pdf failed after retries")


# Send an error payload to the callback URL with basic retries.
def post_error(callback_url: str, message: str) -> None:
    payload = json.dumps({"message": message}).encode("utf-8")
    request = urllib.request.Request(
        callback_url,
        data=payload,
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    for attempt in range(1, MAX_RETRIES + 1):
        try:
            with urllib.request.urlopen(request, timeout=30) as response:
                response.read()
            return
        except Exception as exc:
            logger.error("post_error failed (attempt %s/%s): %s", attempt, MAX_RETRIES, exc)
            if attempt < MAX_RETRIES:
                time.sleep(RETRY_DELAY_SECONDS)


# Purge all pending messages to avoid reprocessing stale jobs.
def purge_queue(channel, reason: str) -> None:
    try:
        channel.queue_purge(queue=settings.report_queue)
        logger.warning("Queue purged (%s)", reason)
    except Exception as exc:
        logger.error("Queue purge failed: %s", exc)


# Consume a RabbitMQ job and generate/post the PDF response.
def process_message(channel, method, properties, body) -> None:
    message = {}
    try:
        message = json.loads(body.decode("utf-8"))
    except Exception as exc:
        logger.error("Invalid JSON message: %s", exc)
        purge_queue(channel, "invalid-json")
        return

    print(f"[analytics-worker] Job received: {message}")

    greenhouse_id = message.get("greenhouse_id")
    callback_url = message.get("callback_url")
    if greenhouse_id is None or callback_url is None:
        logger.error("Missing greenhouse_id or callback_url in message")
        purge_queue(channel, "missing-fields")
        return

    try:
        pdf_bytes = generate_report_pdf(int(greenhouse_id))
        post_pdf(callback_url, pdf_bytes)
    except Exception as exc:
        logger.exception("Error processing job: %s", exc)
        if callback_url:
            try:
                post_error(callback_url, str(exc))
            except Exception as callback_exc:
                logger.error("Failed to notify callback: %s", callback_exc)
        purge_queue(channel, "processing-error")


# Start the worker and keep consuming jobs even after transient errors.
def main() -> None:
    credentials = pika.PlainCredentials(settings.rabbitmq_user, settings.rabbitmq_password)
    params = pika.ConnectionParameters(
        host=settings.rabbitmq_host,
        port=settings.rabbitmq_port,
        credentials=credentials,
    )

    while True:
        try:
            connection = pika.BlockingConnection(params)
            channel = connection.channel()
            channel.queue_declare(queue=settings.report_queue, durable=True)
            purge_queue(channel, "startup")
            channel.basic_qos(prefetch_count=1)
            channel.basic_consume(
                queue=settings.report_queue,
                on_message_callback=process_message,
                auto_ack=True,
            )

            print("[analytics-worker] Waiting for report jobs...")
            channel.start_consuming()
        except KeyboardInterrupt:
            print("[analytics-worker] Stopping worker...")
            break
        except Exception as exc:
            logger.error("Worker loop error: %s", exc)
            time.sleep(RETRY_DELAY_SECONDS)


if __name__ == "__main__":
    main()
