from InquirerPy import inquirer
import click
import requests
from rich.progress import Progress, SpinnerColumn, TextColumn
import pika
import json

GATE_ID = "Lot A"
VISITOR_BASE_URL = "http://localhost:9091/api/visitors/"
TRANSPONDER_BASE_URL = "http://localhost:9090/api/transponders/"
MEMBER_EXIT_BASE_URL = "http://localhost:9093/gate/Lot_A/exit/"

CURRENT_USER_TYPE = "" # VISITOR or MEMBER
WAITING_PROGRESS = None

LOGO = r"""
 ░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓██████████████▓▒░░▒▓████████▓▒░ 
░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░        
░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░        
░▒▓████████▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓██████▓▒░   
░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░        
░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░        
░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░ 
                                                                                                                     

"""

@click.group()
def cli():
    """ACME Parking Kiosk"""
    click.echo(LOGO)
    click.echo("Welcome to the ACME Parking Kiosk CLI!\n")


@cli.command("enter")
def enter():
    """Enter parking lot."""
    click.echo("\n[Enter Parking Lot]")

    # Step 1: Determine if user has a transponder
    user_type = inquirer.select(
        message="Do you have a transponder?",
        choices=["Yes, I'm a member", "No, I'm a visitor"],
    ).execute()

    global CURRENT_USER_TYPE
    if user_type == "Yes, I'm a member":
        # Member flow
        CURRENT_USER_TYPE = "MEMBER"
        transponder_id = click.prompt("Enter your transponder ID")
        license_plate = click.prompt("Enter your license plate")

        # Member request payload
        payload = {
            "transponderId": transponder_id,
            "licensePlate": license_plate,
            "gateId": GATE_ID
        }

        click.echo("\nSending member entry request to the server...")
        try:
            response = requests.put(
                TRANSPONDER_BASE_URL,
                headers={"accept": "*/*", "Content-Type": "application/json"},
                json=payload,
            )
            if response.status_code == 200:
                listen_to_gate_entry()

            else:
                click.echo(f"\nEntry denied. Server responded with: {response.status_code}")
                click.echo(f"Response: {response.text}")
        except requests.RequestException as e:
            click.echo(f"\nError while communicating with the server: {e}")
    else:
        # Visitor flow
        CURRENT_USER_TYPE = "VISITOR"

        license_plate = click.prompt("Enter your license plate")

        # Visitor request payload
        payload = {
            "licensePlate": license_plate,
            "gateId": GATE_ID
            }

        click.echo("\nSending visitor entry request to the server...")

        try:
            response = requests.post(
                VISITOR_BASE_URL,
                headers={"accept": "*/*", "Content-Type": "application/json"},
                json=payload,
            )
            if response.status_code == 202:
                listen_to_gate_entry()
            else:
                click.echo(f"\nEntry denied. Server responded with: {response.status_code}")
                click.echo(f"Response: {response.text}")
        except requests.RequestException as e:
            click.echo(f"\nError while communicating with the server: {e}")


@cli.command("exit")
def exit():
    """Leave parking lot."""
    click.echo("\n[Leave Parking Lot]")

    # Step 1: Determine user identity
    user_type = inquirer.select(
        message="Are you a member or a visitor?",
        choices=["Yes, I'm a member", "No, I'm a visitor"],
    ).execute()

    global CURRENT_USER_TYPE
    if user_type == "Yes, I'm a member":
        # Member flow
        CURRENT_USER_TYPE = "MEMBER"

        license_plate = click.prompt("Enter your license plate")
        # Member exit request payload
        payload = {
            "license": license_plate,
            "gateId": GATE_ID
        }

        click.echo("\nSending member exit request to the server...")
        try:
            response = requests.post(
                MEMBER_EXIT_BASE_URL,
                headers={"accept": "*/*", "Content-Type": "application/json"},
                json=payload,
            )
            if response.status_code == 200:
                # listen_to_gate_exit()
                click.echo("Exit")
            else:
                click.echo(f"\nExit denied. Server responded with: {response.status_code}")
                click.echo(f"Response: {response.text}")
        except requests.RequestException as e:
            click.echo(f"\nError while communicating with the server: {e}")
    else:
        # Visitor flow
        CURRENT_USER_TYPE = "VISITOR"

        visitor_id = click.prompt("Enter your visiter ID")

        license_plate = click.prompt("Enter your license plate")

        has_voucher = click.confirm("Do you have a voucher ID?", default=True)

        if has_voucher:
            voucher_id = click.prompt("Enter your voucher ID")

        # Visitor exit request payload
        payload = {
            "visitorId": visitor_id,
            "voucherId" if has_voucher else None: voucher_id,
            "licensePlate": license_plate,
            "gateId": GATE_ID
        }

        click.echo("\nSending visitor exit request to the server...")
        try:
            response = requests.put(
                VISITOR_BASE_URL,
                headers={"accept": "*/*", "Content-Type": "application/json"},
                json=payload,
            )
            if response.status_code == 200:
                # listen_to_gate_exit()
                click.echo("Exit")
            else:
                click.echo(f"\nExit denied. Server responded with: {response.status_code}")
                click.echo(f"Response: {response.text}")
        except requests.RequestException as e:
            click.echo(f"\nError while communicating with the server: {e}")


def listen_to_gate_entry():
    exchange = "control-gate"
    queue_name = "control.gate.queue"
    routing_key = "*"

    def gate_message_callback(ch, method, properties, body):
        ch.stop_consuming()
        WAITING_PROGRESS.stop()
        
        try:
            message = json.loads(body)
            control_signal = message.get("controlSignal")

            if control_signal == "Access Approval":

                if CURRENT_USER_TYPE == "MEMBER":
                    click.echo("\nEntry allowed! Welcome to the parking lot.")
                else:
                    user_id = message.get("userId")
                    click.echo("\nEntry allowed! Welcome to the parking lot.")
                    click.echo("\n[Visitor Entry] Please note:")
                    click.echo(f"  - Your Visitor ID (QR Code): {user_id}")
                    click.echo("  - Present this QR code at the kiosk when leaving the parking lot.")

            else:
                click.echo("Access denied.")

        except Exception as e:
            click.echo(f"Error processing gate message: {e}")

    credentials = pika.PlainCredentials('admin', 'cas735')
    parameters = pika.ConnectionParameters(host='localhost', credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    channel.exchange_declare(exchange=exchange, exchange_type='topic', durable=True)
    channel.queue_declare(queue=queue_name, durable=True)
    channel.queue_bind(queue=queue_name, exchange=exchange, routing_key=routing_key)

    channel.basic_consume(queue=queue_name, on_message_callback=gate_message_callback, auto_ack=False)

    with Progress(
        SpinnerColumn(),
        TextColumn("[progress.description]{task.description}"),
        transient=True,
    ) as progress:
        global WAITING_PROGRESS
        WAITING_PROGRESS = progress
        progress.add_task("Waiting for gate control message...", total=None)
        channel.start_consuming()


if __name__ == "__main__":
    cli()