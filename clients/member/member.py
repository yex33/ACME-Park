from InquirerPy import inquirer
import click
import requests
from rich.console import Console
from rich.progress import Progress, SpinnerColumn, TextColumn
import time

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
    organization_id = click.prompt("Enter your organization ID (e.g. aa9acb9a-9060-4b0f-bc5b-40cb088350c2)")
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
            wait_for_payment_message(permit_type)
        else:
            click.echo(f"\nFailed to create permit. Server responded with: {response.status_code}")
            click.echo(f"Response: {response.text}")
    except requests.RequestException as e:
        click.echo(f"\nError while communicating with the server: {e}")

def wait_for_payment_message(user_type):
    """Wait for a RabbitMQ message indicating payment information."""
    click.echo("\n[Payment] Waiting for payment information...")

    def simulate_rabbitmq_message():
        # Placeholder for RabbitMQ message simulation
        time.sleep(5)  # Simulate delay before message is received
        return {"paymentRequired": True}

    # Display progress bar while waiting
    with Progress(
        SpinnerColumn(),
        TextColumn("[progress.description]{task.description}"),
        transient=True,
    ) as progress:
        task = progress.add_task("Waiting for payment message...", total=None)
        message = simulate_rabbitmq_message()
        progress.stop()

    click.echo("\n[Payment] Payment information received!")

    if message.get("paymentRequired"):
        handle_payment(user_type)

def handle_payment(user_type):
    """Handle payment based on user type."""
    if user_type.upper() == "STUDENT":
        click.echo("\n[Payment] STUDENT must pay now.")
        process_payment()
    elif user_type.upper() in ["STAFF", "FACULTY"]:
        payment_method = inquirer.select(
            message="Choose payment method:",
            choices=["Pay Now", "Include in Payslip"],
            default="Pay Now",
        ).execute()

        if payment_method == "Pay Now":
            process_payment()
        else:
            click.echo("\n[Payment] Payment will be included in the payslip.")
            process_payment()

def process_payment():
    """Collect payment details and process the payment."""
    click.echo("\n[Payment] Processing payment...")
    payment_method = click.prompt(
        "Select payment method",
        type=click.Choice(["Credit Card", "Debit Card", "PayPal"], case_sensitive=False),
    )
    if payment_method in ["Credit Card", "Debit Card"]:
        card_number = click.prompt("Enter card number")
        expiration_date = click.prompt("Enter expiration date (MM/YY)")
        cvv = click.prompt("Enter CVV")
        click.echo("\n[Payment] Payment successful!")
    elif payment_method == "PayPal":
        paypal_email = click.prompt("Enter your PayPal email")
        click.echo("\n[Payment] Payment successful via PayPal!")


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