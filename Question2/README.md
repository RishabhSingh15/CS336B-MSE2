# Question 2: Library Management REST API (Spring Boot)

## Overview
A Spring Boot REST API for managing library book records using in-memory Java Collections.

### Book Attributes
- `id` (Integer)
- `title` (String)
- `author` (String)
- `category` (String)
- `availableCopies` (Integer)

---

## REST Endpoints

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/books` | Add a new book record | `201 Created` / `409 Conflict` |
| `GET` | `/books` | Retrieve list of all books | `200 OK` |
| `GET` | `/books/{id}` | Retrieve book by its ID | `200 OK` / `404 Not Found` |
| `PUT` | `/books/{id}` | Update book details | `200 OK` / `404 Not Found` |
| `DELETE` | `/books/{id}` | Delete book by ID | `200 OK` / `404 Not Found` |

---

## How to Run

### Using Maven Wrapper or Maven
```powershell
cd Question2
mvn spring-boot:run
```
or:
```powershell
.\mvnw spring-boot:run
```

The server will start on port `8080`.

---

## Testing with PowerShell

### 1. Retrieve all books
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/books" -Method Get
```

### 2. Retrieve book by ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/books/1" -Method Get
```

### 3. Add a new book
```powershell
$body = @{
    id = 10;
    title = "Design Patterns in Practice";
    author = "Robin Vance";
    category = "Software Engineering";
    availableCopies = 8
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/books" -Method Post -Body $body -ContentType "application/json"
```

### 4. Update an existing book
```powershell
$updateBody = @{
    title = "Design Patterns in Practice (2nd Edition)";
    author = "Robin Vance";
    category = "Software Engineering";
    availableCopies = 12
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/books/10" -Method Put -Body $updateBody -ContentType "application/json"
```

### 5. Delete a book
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/books/10" -Method Delete
```

---

## Project Structure
- `src/main/java/com/example/LibraryManagement/model/Book.java`: Entity model with fields, getters, setters, constructors.
- `src/main/java/com/example/LibraryManagement/service/BookService.java`: Business logic managing in-memory collection (`CopyOnWriteArrayList`).
- `src/main/java/com/example/LibraryManagement/controller/BookController.java`: REST controller handling HTTP mapping and status codes.
- `src/main/java/com/example/LibraryManagement/LibraryApplication.java`: Application entry point.
- `AI_PROMPTS_AND_REFLECTIONS.md`: Deliverable 2 submission.
- `VIVA_PREPARATION_GUIDE.md`: Deliverable 3 viva demonstration notes.
