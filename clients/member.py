import uuid
from InquirerPy import inquirer
import click
import requests
from rich.table import Table
import pika
import json
from rabbitmq_utils import listen_to_queue
from payment_utils import listen_to_payment_message

MEMBER_BASE_URL = "http://localhost:9090/api/permits/"
VISITOR_BASE_URL = "http://localhost:9091/api/vouchers/"

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
    """ACME Parking Member Management CLI"""
    click.echo(LOGO)
    click.echo("Welcome to the ACME Parking Member Management CLI!\n")


@cli.group()
def permit():
    """Manage parking permits."""
    pass


@permit.command("new")
def new_permit():
    """Create a new parking permit."""
    click.echo("\n[Permit New] Create a new parking permit.")
    organization_id = click.prompt(
        "Enter your organization ID",
        default=uuid.uuid4()
    )
    license_plates = click.prompt(
        "Enter license plates (comma-separated) (e.g. CBS221)",
        value_proc=lambda x: [plate.strip() for plate in x.split(",")]
    )

    permit_type = inquirer.select(
        message="Select the permit type:",
        choices=["STUDENT", "STAFF", "FACULTY"],
        default="STUDENT",
    ).execute()

    permit_data = {
        "organizationId": organization_id,
        "userType": permit_type.upper(),
        "licensePlates": license_plates,
        "isRenew": False,
    }

    click.echo("\n[Permit New] Permit request details:")
    click.echo(f"  Organization ID: {permit_data['organizationId']}")
    click.echo(f"  User Type: {permit_data['userType']}")
    click.echo(f"  License Plates: {permit_data['licensePlates']}")

    click.echo("\nSending request to the server...")

    try:
        response = requests.post(
            MEMBER_BASE_URL,
            headers={
                "accept": "*/*",
                "Content-Type": "application/json"
            },
            json=permit_data
        )
        if response.status_code == 201:
            listen_to_payment_message(on_payment_complete=listen_to_transponder_message)

        else:
            click.echo(f"\nFailed to create permit. Server responded with: {response.status_code}")
            click.echo(f"Response: {response.text}")
    except requests.RequestException as e:
        click.echo(f"\nError while communicating with the server: {e}")


def listen_to_transponder_message():

    def payment_message_callback(body):
        click.echo("\nTransponder issued successfully!")
        click.echo(f"Transponder ID: {body.decode("utf-8")}")

    listen_to_queue(exchange="transponder.event", queue_name="transponder.event.queue", callback=payment_message_callback, task_description="Waiting for transponder message...")


@cli.command("voucher")
def visitor_voucher():
    """Issue a visitor parking voucher."""
    click.echo("\n[Visitor Voucher] Issue a visitor parking voucher.")
    license_plate = click.prompt("Enter the visitor's license plate (e.g. HBO987)")

    click.echo("\n[Visitor Voucher] Voucher details:")
    click.echo(f"  License Plate: {license_plate}")

    click.echo("\nSending request to the server...")

    try:
        response = requests.post(
            VISITOR_BASE_URL,
            headers={
                "accept": "*/*",
                "Content-Type": "application/json"
            },
            json={"licensePlate": license_plate}
        )
        if response.status_code == 201:
            voucher_code = response.text

            click.echo("\nVisitor voucher issued successfully!")
            click.echo(f"Voucher Code: {voucher_code}")

            click.echo("\nInstructions for Voucher Redemption:")
            click.echo("- Redeem the code when exiting parking lots.")
            click.echo("- Show the QR code or the voucher code at the kiosk or to the attendant.")
        else:
            click.echo(f"\nFailed to issue voucher. Server responded with: {response.status_code}")
            click.echo(f"Response: {response.text}")
    except requests.RequestException as e:
        click.echo(f"\nError while communicating with the server: {e}")


if __name__ == "__main__":
    cli()