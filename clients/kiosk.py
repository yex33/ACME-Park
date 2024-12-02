import pika
import json


def send_member_existing_gate_command(exchange: str, routing_key: str, message: dict):

    credentials = pika.PlainCredentials('admin', 'cas735')
    parameters = pika.ConnectionParameters(host='localhost', credentials=credentials)
    connection = pika.BlockingConnection(parameters=parameters)
    channel = connection.channel()
    channel.exchange_declare(exchange=exchange , exchange_type='topic', durable=True)

    # Publish the message
    channel.basic_publish(
        exchange=exchange,
        routing_key=routing_key,
        body=json.dumps(message),
        properties=pika.BasicProperties(delivery_mode=2)  # Persistent messages
    )

    print(f"Message sent to exchange '{exchange}' with routing key '{routing_key}': {message}")

    connection.close()

def main():
    exchange = "exit"
    routing_key = "*"
    member_exit_request = {
        "gate_id": "Lot M",
        "license_plate": "squeeze"
    }

    send_member_existing_gate_command(exchange, routing_key, member_exit_request)

if __name__ == "__main__":
    main()