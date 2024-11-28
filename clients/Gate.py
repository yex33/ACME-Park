import pika

credentials = pika.PlainCredentials('admin', 'cas735')
parameters = pika.ConnectionParameters(credentials=credentials)

connection = pika.BlockingConnection(parameters=parameters)
channel = connection.channel()

def callback(ch, method, properties, body):
    print(f"{body}")
    print("Received  approval signal")
    channel.stop_consuming()

def receive(exchange : str, queue_name : str, routing_key: str):
    channel.queue_declare(queue=queue_name, durable=True)
    channel.queue_bind(queue=queue_name, exchange=exchange, routing_key=routing_key)
    try:
        channel.basic_consume(queue=queue_name, auto_ack=True, on_message_callback=callback)
        channel.start_consuming()
    except pika.exceptions.AMQPChannelError:
        pass

def main():
    exchange = "approval.sender"
    queue_name = "approval.sender.queue"
    routing_key = "*"
    receive(exchange, queue_name, routing_key)
    connection.close()

if __name__ == "__main__":
    main()
