# Deliverable 3: Viva Preparation Guide

## 1. Business Logic Overview
The Library Management REST API manages book records in an academic or institutional library. It provides full lifecycle management (CRUD):
- **Create**: Add new book records with initial available copy counts.
- **Read**: View the entire catalog or search for an individual title by its unique identifier.
- **Update**: Adjust catalog metadata (title, author, category) or update available stock count.
- **Delete**: Remove obsolete or decommissioned books from the collection.

---

## 2. Collections Framework Usage & Justification

### Data Structure Selection
In this application, book entities are managed in-memory using the Java Collections Framework:
```java
private final List<Book> bookList = new CopyOnWriteArrayList<>();
```

### Why `List<Book>` / `CopyOnWriteArrayList`?
1. **Thread Safety**: Spring Boot handles HTTP requests concurrently on multiple threads. `CopyOnWriteArrayList` allows thread-safe iteration and updates without throwing `ConcurrentModificationException`.
2. **Order Preservation**: Maintains insertion order, allowing predictable index-based listing and sorting.
3. **Stream API Integration**: Works seamlessly with Java 8+ functional streams for filtering, mapping, and searching:
   ```java
   bookList.stream()
           .filter(b -> b.getId().equals(id))
           .findFirst();
   ```
4. **Conditional Removal**:
   ```java
   bookList.removeIf(b -> b.getId().equals(id));
   ```
   This atomically checks each element and removes the matching entity in linear time $O(n)$.

---

## 3. Spring Boot Annotations Explained

| Annotation | Purpose | Where Used |
| :--- | :--- | :--- |
| `@RestController` | Combines `@Controller` and `@ResponseBody`. Tells Spring to serialize return values directly to JSON. | `BookController` |
| `@RequestMapping` | Maps base URI prefix (`/books`) to all handlers in the class. | `BookController` |
| `@PostMapping` | Handles incoming HTTP `POST` requests to create new resources. | `addBook()` |
| `@RequestBody` | Binds the HTTP JSON request body to the method parameter (`Book`). | `addBook()`, `updateBook()` |
| `@GetMapping` | Handles incoming HTTP `GET` requests to retrieve data. | `getAllBooks()`, `getBookById()` |
| `@PathVariable` | Extracts URI path variables (e.g. `{id}`) directly into method parameters. | `getBookById()`, `updateBook()`, `deleteBook()` |
| `@PutMapping` | Handles incoming HTTP `PUT` requests to perform complete updates on existing resources. | `updateBook()` |
| `@DeleteMapping` | Handles incoming HTTP `DELETE` requests to delete resources. | `deleteBook()` |
| `@Service` | Marks the class as a business service bean managed by Spring's IoC container. | `BookService` |

---

## 4. HTTP Status Code Conventions
- `200 OK`: Request succeeded (GET, PUT, DELETE with body).
- `201 Created`: Resource successfully created (POST).
- `400 Bad Request`: Invalid payload or missing parameters.
- `404 Not Found`: Requested ID does not exist in the collection.
- `409 Conflict`: Attempting to insert a book with an ID that already exists.
