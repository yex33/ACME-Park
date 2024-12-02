# Lot Management

- Author: Qianlin Chen
- Version 2024.11

## Rationale

The `lot_management` service is a core component of the ACME PARK system, responsible for managing parking lot operations. It facilitates services starting with gate access requests, handling both entry and exit records, and updating user and vehicle data accordingly. The service also ensures smooth coordination by integrating with controllers to issue fines, generate parking analytics

## Technologies

- Java 23 (LTS)
- Spring Boot 3.3.2
- RabbitMQ (AMQP)
- REST