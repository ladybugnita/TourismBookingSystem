# Tourism Booking System (Backend)

A backend REST API for managing tourism package bookings, with **eSewa payment gateway integration**. Built with Java 17, Spring Boot, Hibernate, and MySQL.

---

## Overview

Nepal attracts travellers from around the world, but many local tour operators still handle bookings through phone calls, messages, and cash payments, which makes packages hard to manage and bookings easy to lose track of. This project provides the backend for an online booking platform: users can browse and book tourism packages and pay digitally through **eSewa**, one of Nepal's most widely used digital wallets, while admins manage packages and monitor all bookings from one system.

---

## Key Features

**For users:**
- View available tourism packages
- Book a package, and edit or cancel their bookings
- Pay for bookings through eSewa

**For admins:**
- Add, edit, and delete tourism packages
- View all bookings made by users

**Payments:**
- Integrated with the **eSewa payment gateway** (sandbox mode)
- Payment verification endpoints handle successful and failed transactions

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| ORM | Hibernate (JPA) |
| Database | MySQL (set up with MySQL Workbench 8.0 CE) |
| Payment Gateway | eSewa (sandbox) |
| API Testing | Postman |

---

## Project Structure

```
src/main/java/.../
├── controller/    REST endpoints for packages, bookings, and payments
├── service/       Business logic (booking rules, payment verification)
├── repository/    Database access with Spring Data JPA / Hibernate
├── entity/        Database models (package, booking, user, payment)
├── payload/       Request and response objects (DTOs) for the APIs
├── security/      JWT authentication and role-based access for users and admins
└── config/        Application and eSewa configuration

src/main/resources/
└── application.properties    Database and application settings
```

---

## Key API Endpoints


| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/payment/success` | Verifies a successful eSewa payment |
| GET | `/api/payment/failure` | Handles a failed or cancelled eSewa payment |

All REST APIs were tested with Postman.

---

## Installation & Setup

### Prerequisites
- Java 17
- Maven
- MySQL

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/ladybugnita/TourismBookingSystem.git
   cd TourismBookingSystem
   ```
2. Create a MySQL database and update your database credentials in `src/main/resources/application.properties`.
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Test the APIs with Postman at `http://localhost:8080`.

> **Note:** eSewa runs in sandbox (test) mode, so no real payments are made.

---

## Future Improvements

- Build a React frontend so users can browse and book packages through a web interface
- Support more payment options, such as Khalti
- Send booking confirmation emails to users

---

## Contributing

Suggestions and improvements are welcome. Feel free to fork the repository, create a feature branch, and open a pull request.

---

## Author

**Nita Dangol**
[GitHub](https://github.com/ladybugnita) · [LinkedIn](https://linkedin.com/in/nitadangol) · [Portfolio](https://nitadangol.com.np)
