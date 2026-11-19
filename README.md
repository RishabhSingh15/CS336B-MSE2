# CS336B - Object Oriented Programming with Java

This repository contains implementations of two core projects:
1. **Order Management System** (Java JDBC & MySQL)
2. **Library Management System** (Spring Boot REST API)

---

## Repository Structure

```text
.
├── OrderManagementSystem/              # E-Commerce Order Management System (JDBC + MySQL)
│   ├── OrderManagementSystem.java      # Menu-driven Java JDBC application
│   ├── schema.sql                      # Database DDL & initial records (ecommerce_db)
│   ├── pom.xml                         # Maven build file
│   ├── run.bat / run.ps1               # Quick run scripts
│   └── lib/                            # MySQL Connector/J driver
│
└── LibraryManagement/                  # Library Management REST API (Spring Boot)
    ├── src/main/java/com/example/LibraryManagement/
    │   ├── model/Book.java             # Entity model (id, title, author, category, copies)
    │   ├── controller/BookController.java  # REST controller (CRUD operations)
    │   ├── service/BookService.java    # Business logic with Java Collections Framework
    │   └── LibraryApplication.java     # Spring Boot application entry point
    ├── pom.xml                         # Spring Boot 3.3.5 Maven configuration
    ├── mvnw / mvnw.cmd                 # Maven wrapper scripts
    └── LibraryManagement.postman_collection.json # Postman test collection
```

---

## 1. Order Management System (Java JDBC & MySQL)
- **Database:** `ecommerce_db` | **Table:** `Orders`
- **Features:**
  - `PreparedStatement` record insertion with duplicate primary key handling (`SQLIntegrityConstraintViolationException`).
  - Filter orders by status where `quantity > 2`, sorted by `unit_price DESC`.
  - Increase existing order quantity with validation.
  - Update status with validation (`Pending`, `Shipped`, `Delivered`).
  - Total order value calculation using `SUM(quantity * unit_price)` and conditional deletion restricting to `Cancelled` orders only.

### How to Run:
```powershell
cd OrderManagementSystem
.\run.bat
```

---

## 2. Library Management System (Spring Boot REST API)
- **Framework:** Spring Boot 3.3.5
- **Storage:** In-memory Java Collections Framework (`CopyOnWriteArrayList<Book>`)
- **Endpoints:**
  - `POST /books` — Create new book record (`201 Created` / `409 Conflict`)
  - `GET /books` — Retrieve all books (`200 OK`)
  - `GET /books/{id}` — Retrieve specific book by ID (`200 OK` / `404 Not Found`)
  - `PUT /books/{id}` — Update book details (`200 OK` / `404 Not Found`)
  - `DELETE /books/{id}` — Delete book by ID (`200 OK` / `404 Not Found`)

### How to Run:
```powershell
cd LibraryManagement
mvn spring-boot:run
```
*(Server starts on `http://localhost:8080/books`)*
