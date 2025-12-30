# Naples Airport Management System — Java Desktop Application
  ![Type](https://img.shields.io/badge/type-University%20Project-orange)

University group project developed for the Object-Oriented Programming course  
B.Sc. in Computer Science — University of Naples Federico II (A.Y. 2024/2025)

This repository contains a Java Swing desktop application backed by a PostgreSQL
relational database for managing core airport operations: flights, bookings,
baggage tracking, and user access control.

> Note: The repository structure follows the official coursework template required by the academic assignment.

## Project Overview

The application supports role-based access for:

- **Generic users**:
  - view scheduled flights
  - create and modify reservations
  - consult baggage status
- **Administrators**:
  - all generic user features
  - create/update flight details
  - manage bookings and baggage

Additional features include:
- real-time flight overview with delays/cancellations
- search and filtering on flights, bookings, passengers, and baggage

## Core Functionality

### Authentication & Roles
- Login with credentials
- Two roles: generic user and administrator

### Flight Management
- Browse flights
- Create and update flight information
- Assign gates and update statuses

### Booking System
- Ticket creation with seat assignment
- Search and modify bookings

### Baggage Handling
- Track baggage state (processing, loaded, available, lost)
- Lost baggage reporting and recovery workflows

### Operational Overview
- Dashboard view of arrivals/departures
- Highlights delays and cancellations

## Tech Stack

- Java (Swing) — desktop UI
- PostgreSQL — database
- JDBC — database connectivity
- Maven — build and dependency management
- UML diagrams + technical documentation

## Documentation
The repository includes comprehensive documentation written in Italian:
- UML diagrams (conceptual/logical, class diagrams, sequence diagrams)
- GUI interaction manual
- error handling notes

Files are available under:
```bash
documentazione/
```
## Contribution Overview

This project was developed collaboratively by the team, with shared ownership across all components.

All team members contributed across the overall system.

## Authors
- Carmine Sgariglia
- Mattia Lemma
- Massimo Russo
