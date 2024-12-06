# ACME Parking System CLI

The ACME Parking System CLI provides two essential tools for managing parking operations:

1. Member Management (`member.py`): Handles parking permits for members and issues visitor vouchers.
2. Kiosk Management (`kiosk.py`): Facilitates parking lot entry and exit for both members and visitors.
3. Officer Management (`officers.py`): Enables parking officers to issue fines for violations.

## Introduction

The ACME Parking System CLI tools are designed to streamline parking management:

- Member CLI focuses on managing parking permits and issuing temporary visitor vouchers.
- Kiosk CLI supports real-time entry and exit workflows for both members and visitors.
- Officer CLI handles parking violations by issuing fines.

## Features

### Member Management (`member.py`)
- Create new parking permits for members (e.g., students, staff, faculty).
- Issue visitor vouchers for temporary parking.

### Kiosk (`kiosk.py`)
- Facilitate parking lot entry for members using transponders and visitors with license plates.
- Validate parking lot exits using visitor IDs or transponders.

### Officer (`officers.py`)
- Issue fines for parking violations, specifying license plates, descriptions, and fine amounts.

## Installation

### Prerequisites
- Python 3.12 or higher
- `pipenv` for dependency management
- Whole ACME Parking server running on docker

### Steps

1. Install dependencies:
```bash
pipenv install
```
2. Activate the virtual environment:
```bash
pipenv shell
```

## Usage

### Member Management CLI (`member.py`)

__Commands__

1. __Create a New Permit__:

```bash
python member.py permit new
```

- Follow prompts to:
    - Enter your organization ID, which is the unique identifier of each student, staff and facluty member.
    - Provide license plates (comma-separated).
    - Select permit type (STUDENT, STAFF, or FACULTY).

The script processes payment and provides transponder details.

2. __Issue a Visitor Voucher__:

```bash
python member.py voucher
```

- Enter the visitor's license plate.
- A voucher code is generated for redemption upon exit.

### Kiosk CLI (`kiosk.py`)

The kiosk.py script manages parking lot entry and exit workflows for visitors and members.

__Commands__

1. __Enter Parking Lot__:

```bash
python kiosk.py enter
```

- __Members__:
    - Provide transponder ID and license plate.
    - The gate control system validates entry in real-time.

- __Visitors__:
    - Provide license plate.
    - Receive a Visitor ID (QR Code) for validation during exit.

2. __Leave Parking Lot__:

```bash
python kiosk.py exit
```

- __Members__:
    - Provide license plate.
    - Gate control validates the exit.

- __Visitors__:
    - Provide Visitor ID (QR Code) and optional voucher code.
    - Payment is processed if needed.

### Officer CLI (`officers.py`)

__Commands__:

1. Issue a Fine:

```bash
python officers.py issue
```

- Follow prompts to:
    - Enter the vehicle’s license plate.
	- Provide a description for the fine.
	- Specify the fine amount.
