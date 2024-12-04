import json
import click
from rich.console import Console
from rich.table import Table
from rich.progress import Progress, SpinnerColumn, TextColumn
from InquirerPy import inquirer
from rabbitmq_utils import publish_message, listen_to_queue

console = Console()

def listen_to_payment_message(on_payment_complete=None):
    """
    Listen for payment messages on a RabbitMQ queue.
    """
    def handle_payment_message(message):
        """
        Callback function for processing payment messages.
        """
        try:
            message = json.loads(message.decode("utf-8"))
            invoice = message.get("invoice", {})
            user_type = invoice.get("user", {}).get("userType", "UNKNOWN")
            click.echo("\n[Payment Message] Invoice received.")
            handle_payment(user_type, message, on_payment_complete)
        except Exception as e:
            click.echo(f"\nError processing payment message: {e}")

    listen_to_queue(
        exchange="payment.method.selection.request",
        queue_name="payment.method.selection.request.queue",
        callback=handle_payment_message,
        task_description="Waiting for payment message..."
    )


def handle_payment(user_type, message, on_payment_complete=None):
    """
    Handle payment based on user type and invoice details.

    Args:
        user_type (str): Type of user (e.g., "STUDENT", "STAFF", "FACULTY", "VISITOR").
        message (dict): Payment message data.
    """
    invoice = message.get("invoice", {})
    charges = invoice.get("charges", [])
    payment_methods = message.get("paymentMethods", [])
    invoice_id = message.get("invoiceId")

    # Display charges in a table
    show_table(
        f"Invoice #{invoice_id}",
        ["Transaction ID", "Type", "Amount", "Issued On"],
        [
            [
                charge.get("transactionId", ""),
                charge.get("transactionType", ""),
                f"${charge.get('amount', 0) / 100:.2f}",
                "-".join(map(str, charge.get("issuedOn", []))),
            ]
            for charge in charges
        ],
    )

    # Payment decision based on user type
    if user_type.upper() in ["STUDENT", "VISITOR"]:
        click.echo(f"\n[Payment] {user_type.upper()} must pay now.")
        process_payment(invoice_id, "UPFRONT_PAYMENT", on_payment_complete)
    elif user_type.upper() in ["STAFF", "FACULTY"]:
        selected_payment_method = inquirer.select(
            message="Choose payment method:",
            choices=payment_methods,
            default=payment_methods[0],
        ).execute()

        if selected_payment_method == "UPFRONT_PAYMENT":
            click.echo("\n[Payment] You chose to pay now.")
        elif selected_payment_method == "RESERVE_IN_PAYSLIP":
            click.echo("\n[Payment] You chose to include this in your payslip.")

        process_payment(invoice_id, selected_payment_method, on_payment_complete)


def process_payment(invoice_id, method, on_payment_complete=None):
    """
    Process the payment and publish the result.

    Args:
        invoice_id (int): Invoice ID.
        method (str): Payment method.
    """
    click.echo("\n[Payment] Processing payment...")

    if method == "UPFRONT_PAYMENT":
        payment_method = inquirer.select(
        message="Select payment method:",
        choices=["Credit Card", "Debit Card", "PayPal"],
        ).execute()

        if payment_method in ["Credit Card", "Debit Card"]:
            card_number = click.prompt("Enter card number")
            expiration_date = click.prompt("Enter expiration date (MM/YY)")
            cvv = click.prompt("Enter CVV")
            click.echo(f"\n[Payment] Payment successful using {payment_method}!")
        elif payment_method == "PayPal":
            paypal_email = click.prompt("Enter your PayPal email")
            click.echo("\n[Payment] Payment successful via PayPal!")


    # Publish payment result
    message = {
        "invoiceId": invoice_id,
        "paymentMethod": method,
    }
    publish_message(
        exchange="payment.method.selection",
        routing_key="#",
        message=message,
    )

    click.echo("\n[Payment] Payment process completed.")

    if on_payment_complete:
        on_payment_complete()


def show_table(title, headers, rows):
    """
    Display a table using Rich.

    Args:
        title (str): Table title.
        headers (list): List of header strings.
        rows (list): List of row data.
    """
    table = Table(title=title, show_header=True, header_style="bold cyan")
    for header in headers:
        table.add_column(header)
    for row in rows:
        table.add_row(*row)
    console.print(table)
