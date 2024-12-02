import pika
import json

def callback(ch, method, body):
    try:
        message = json.loads(body)
        print(f"Received message: {message}")

        control_signal = message.get("controlSignal")
        gate_id = message.get("gateId")

        print(f"Control Signal: {control_signal}, Gate ID: {gate_id}")
        print("Processing control gate result...")

    except Exception as e:
        print(f"Error processing message: {e}")
    finally:
        ch.basic_ack(delivery_tag=method.delivery_tag)

def receive(exchange: str, queue_name: str, routing_key: str):
    credentials = pika.PlainCredentials('admin', 'cas735')  # Replace with actual credentials
    parameters = pika.ConnectionParameters(host='localhost', credentials=credentials)  # Replace with your RabbitMQ host
    connection = pika.BlockingConnection(parameters=parameters)
    channel = connection.channel()

    channel.exchange_declare(exchange=exchange, exchange_type='topic', durable=True)
    channel.queue_declare(queue=queue_name, durable=True)
    channel.queue_bind(queue=queue_name, exchange=exchange, routing_key=routing_key)

    print(f"Waiting for messages on queue '{queue_name}'...")
    channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=False)

    try:
        channel.start_consuming()
    except KeyboardInterrupt:
        print("Stopping the consumer...")
        channel.stop_consuming()
    finally:
        connection.close()

def main():
    exchange = "control-gate"
    queue_name = "control.gate.queue"
    routing_key = "*"

    receive(exchange, queue_name, routing_key)

if __name__ == "__main__":
    main()
