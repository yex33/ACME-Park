[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/K7y1ycyn)

# ACME Parking System

- Authors: Joe Ye, Maris Chen, Leonard Chen
- Version: 2024.12

## Abstract

This repository is the final project system for **CAS 735 - (Micro-)Service Oriented Architecture**, a graduate course offered by the _Computing And Software_ department at the Faculty of Engineering of McMaster University.

## Overall Description

- `infrastructure`: contains the _technical_ services used to support the ACME Parking system:
    - `message_broker`: A RabbitMQ server to support event-driven architectures using MQTT and AMQP protocols.
    - `service_registry`: An Eureka server (Netflix) to support service discovery
- `services`: Business services that implement core parking management functionalities
    - `lot_management`: Manages parking lots and gate operations, including entry and exit controls.
    - `member_identification`: Handles permit registration, and transponder identification processes.
    - `parking_enforcement`: Issues fines for parking violations.
    - `payment_processing`: Processes payments for permits, visitor parking, and fines.
    - `visitor_identification`: Issues visitor IDs and vouchers, and manages temporary parking access.
- `clients`: External (non-service-based artefacts) consuming the ACME Parking system
    - `kiosk`: A CLI tool for managing parking lot entry and exit.
    - `member`: A CLI tool for permit creation, and visitor voucher issuance.
    - `officers`: A CLI tool for parking officers to issue fines for violations.

## Services location

**If you're on a Mac**, you might encounter issues using `localhost` (it might take some time to be available, or be blocked once and foor all). You can always refer to the direct IP address of localhost (`127.0.0.1`) to bypass this situation.

- Infrastructure:
    - RabbitMQ Management web interface: <http://localhost:8080> (login: `admin`, password: `cas735`)
    - Eureka registry: <http://locahost:8761>
- Business Services:
    - Lot Management Service: <http://locahost:9093>
    - Member Identification Service: <http://locahost:9090>
    - Visitor Identification Service: <http://locahost:9091>
    - Parking Enforcement Service: <http://locahost:9080>
    - Payment Processing Service: <http://locahost:9081>

## How to operate?

### Compiling the micro-services

```
group-project-old6 $ mvn clean package
```

### Building the turn-key services

```
group-project-old6 $ cd deployment
deployment $ docker compose build --no-cache
```

### Starting the complete system

```
deployment $ docker compose up -d
```

### Shutting down the system

```
deployment $ docker compose down
```

## How to use the client?

The system provides three client tools: kiosk, member, and officers. Each client interacts with specific services through REST APIs and RabbitMQ for event-driven communication. These clients are located in the `/clients` directory.

For detailed instructions on how to use each client, refer to the README.md file inside `/clients`.

### Parking Lot Access Rules

Each parking lot has specific access rules based on user types. The current configuration is as follows:

| Parking Lot | Allowed User Types                |
|-------------|-----------------------------------|
| Lot M       | STUDENT, STAFF                    |
| Lot A       | FACULTY, VISITOR                  |
| Lot B       | STUDENT, FACULTY                  |
| Lot C       | VISITOR                           |

The `kiosk.py` script inside `/clients` uses `Lot A` as the default parking lot example.