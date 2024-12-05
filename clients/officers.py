import click
import requests
import json

BASE_URL = "http://localhost:9093/issue/"

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
    """ACME Parking Officer CLI"""
    click.echo(LOGO)
    click.echo("Welcome to the ACME Parking Officer CLI!\n")


@cli.command("issue")
def issue_fine():
    """Issue a fine to a vehicle."""
    click.echo("\n[Issue Fine]")

    # Get license plate
    license_plate = click.prompt("Enter the license plate of the vehicle", type=str)

    # Get fine details
    description = click.prompt("Enter a description for the fine", type=str)
    amount = click.prompt("Enter the fine amount", type=int)

    # Prepare payload
    payload = {
        "description": description,
        "amount": amount,
    }

    click.echo("\nSending fine issuance request to the server...")
    try:
        # Send POST request
        response = requests.post(
            url=f"{BASE_URL}{license_plate}",
            headers={"Content-Type": "application/json"},
            data=json.dumps(payload),
        )

        # Handle response
        if response.status_code == 202:
            click.echo("\nFine issued successfully!")
        else:
            click.echo("\nFailed to issue fine.")
            click.echo(f"HTTP Status Code: {response.status_code}")
            click.echo(f"Response: {response.text}")
    except requests.RequestException as e:
        click.echo(f"\nError communicating with the server: {e}")


if __name__ == "__main__":
    cli()