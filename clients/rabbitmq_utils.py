import json
import pika
from rich.progress import Progress, SpinnerColumn, TextColumn

RABBIT_MQ_HOST = "localhost"
RABBIT_MQ_USERNAME = "admin"
RABBIT_MQ_PASSWORD = "cas735"

WAITING_PROGRESS = None

def publish_message(exchange, routing_key, message):
    """Publish a message to RabbitMQ."""
    credentials = pika.PlainCredentials(RABBIT_MQ_USERNAME, RABBIT_MQ_PASSWORD)
    parameters = pika.ConnectionParameters(host=RABBIT_MQ_HOST, credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()
    channel.exchange_declare(exchange=exchange, exchange_type="topic", durable=True)
    channel.basic_publish(
        exchange=exchange,
        routing_key=routing_key,
        body=json.dumps(message),
        properties=pika.BasicProperties(content_type="application/json", delivery_mode=2),
    )
    connection.close()

def listen_to_queue(exchange, queue_name, callback, task_description):
    """Listen to a RabbitMQ queue."""

    credentials = pika.PlainCredentials(RABBIT_MQ_USERNAME, RABBIT_MQ_PASSWORD)
    parameters = pika.ConnectionParameters(host=RABBIT_MQ_HOST, credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()
    channel.exchange_declare(exchange=exchange, exchange_type="topic", durable=True)
    channel.queue_declare(queue=queue_name, durable=True)
    channel.queue_bind(queue=queue_name, exchange=exchange, routing_key="#")

    def wrapped_callback(ch, method, properties, body):
        ch.stop_consuming()
        connection.close()
        WAITING_PROGRESS.stop()
        try:
            callback(body)
        except Exception as e:
            print(f"\nError processing message: {e}")

    channel.basic_consume(queue=queue_name, on_message_callback=wrapped_callback, auto_ack=True)

    with Progress(SpinnerColumn(), TextColumn("[progress.description]{task.description}"), transient=True) as progress:
        global WAITING_PROGRESS
        WAITING_PROGRESS = progress
        progress.add_task(task_description, total=None)
        channel.start_consuming()