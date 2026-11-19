# CS336B - Object Oriented Programming with Java (OOPJ)
## Mid Semester Examination-II (ODD SEM 2026-27)

**Student Name:** Rishabh Singh  
**Roll Number:** 202501100600138  
**Branch:** Information Technology (IT)  
**Section:** C | **Shift:** 1  

---

## Repository Structure

```text
CS336B-MSE2/
├── Question1/                          # Question 1: Order Management System (JDBC + MySQL)
│   ├── OrderManagementSystem.java      # Java JDBC Menu-driven application
│   ├── schema.sql                      # Database DDL & seed data (ecommerce_db)
│   ├── pom.xml                         # Maven build configuration
│   ├── run.bat / run.ps1               # 1-click execution scripts
│   └── lib/                            # MySQL Connector/J driver library
│
├── Question2/                          # Question 2: Library Management REST API (Spring Boot)
│   ├── src/main/java/com/example/LibraryManagement/
│   │   ├── model/Book.java             # Entity model (id, title, author, category, copies)
│   │   ├── controller/BookController.java  # REST controller (@PostMapping, @GetMapping, @PutMapping, @DeleteMapping)
│   │   ├── service/BookService.java    # Business logic with Java Collections Framework
│   │   └── LibraryApplication.java     # Spring Boot application entry point
│   ├── pom.xml                         # Spring Boot 3.3.5 Maven configuration
│   ├── mvnw / mvnw.cmd                 # Maven wrapper scripts
│   └── LibraryManagement.postman_collection.json # 1-click Postman test collection
│
└── Submission/                         # Submission Deliverables & Viva Guides
    ├── 202501100600138_RishabhSingh_MSE2_Submission.docx
    ├── Q1_VIVA_NOTES.md
    ├── Q2_AI_PROMPTS_AND_REFLECTIONS.md
    └── Q2_VIVA_PREPARATION_GUIDE.md
```

---

## Question 1: Order Management System (Java JDBC)
- **Database:** `ecommerce_db` | **Table:** `Orders`
- **Features Implemented:**
  - **(a)** `PreparedStatement` record insertion with duplicate primary key handling (`SQLIntegrityConstraintViolationException`).
  - **(b)** Filter orders by status where `quantity > 2`, sorted by `unit_price DESC`.
  - **(c)** Increase existing order quantity by given amount with existence validation.
  - **(d)** Update status only if record exists and status is valid (`Pending`, `Shipped`, `Delivered`).
  - **(e)** Total order value calculation using `SUM(quantity * unit_price)` and conditional deletion restricting to `Cancelled` status only.

### How to Run Question 1:
```powershell
cd Question1
.\run.bat
```

---

## Question 2: Library Management REST API (Spring Boot)
- **Framework:** Spring Boot 3.3.5
- **Storage:** In-memory Java Collections Framework (`CopyOnWriteArrayList<Book>`)
- **Endpoints:**
  - `POST /books` — Create new book record (`201 Created` / `409 Conflict`)
  - `GET /books` — Retrieve all books (`200 OK`)
  - `GET /books/{id}` — Retrieve specific book by ID (`200 OK` / `404 Not Found`)
  - `PUT /books/{id}` — Update book details (`200 OK` / `404 Not Found`)
  - `DELETE /books/{id}` — Delete book by ID (`200 OK` / `404 Not Found`)

### How to Run Question 2:
```powershell
cd Question2
mvn spring-boot:run
```
*(Runs on `http://localhost:8080/books`)*
