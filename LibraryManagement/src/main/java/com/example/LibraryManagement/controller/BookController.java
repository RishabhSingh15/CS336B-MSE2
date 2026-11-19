package com.example.LibraryManagement.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LibraryManagement.model.Book;
import com.example.LibraryManagement.service.BookService;

/**
 * CS336B - Object Oriented Programming with Java
 * Question 2: REST Controller for Library Management
 *
 * Implements:
 *  - Requirement (b): @PostMapping with @RequestBody to add a new book
 *  - Requirement (c): @GetMapping to retrieve all books and @GetMapping("/{id}") to retrieve by ID
 *  - Requirement (d): @PutMapping("/{id}") to update book details
 *  - Requirement (e): @DeleteMapping("/{id}") to delete a book with appropriate HTTP responses
 */
@RestController
@RequestMapping({"/books", "/api/books"})
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Requirement (c): Retrieve all books.
     * HTTP Method: GET /books or /api/books
     * Response: 200 OK with JSON array of books.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Requirement (c): Retrieve a specific book by its ID.
     * HTTP Method: GET /books/{id} or /api/books/{id}
     * Response: 200 OK if found, 404 NOT_FOUND if book doesn't exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }

        return buildErrorResponse(HttpStatus.NOT_FOUND, "Book not found with ID: " + id);
    }

    /**
     * Requirement (b): Add a new book using @PostMapping and @RequestBody.
     * HTTP Method: POST /books or /api/books
     * Request Body: JSON object matching Book fields
     * Response: 201 CREATED with created Book, or 409 CONFLICT if ID already exists.
     */
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (IllegalStateException e) {
            return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Requirement (d): Update a book's details using @PutMapping.
     * HTTP Method: PUT /books/{id} or /api/books/{id}
     * Request Body: JSON with updated title, author, category, availableCopies
     * Response: 200 OK with updated Book, or 404 NOT_FOUND if book doesn't exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book updatedBook) {
        Optional<Book> result = bookService.updateBook(id, updatedBook);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }

        return buildErrorResponse(HttpStatus.NOT_FOUND, "Cannot update. Book not found with ID: " + id);
    }

    /**
     * Requirement (e): Delete a book by its ID using @DeleteMapping.
     * HTTP Method: DELETE /books/{id} or /api/books/{id}
     * Response:
     *   - 200 OK with confirmation message when deletion is successful.
     *   - 404 NOT_FOUND when the book is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Book with ID " + id + " has been successfully deleted.");
            return ResponseEntity.ok(response);
        }

        return buildErrorResponse(HttpStatus.NOT_FOUND, "Cannot delete. Book not found with ID: " + id);
    }

    /**
     * Helper method to construct standardized JSON error responses.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().toString());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);
        return ResponseEntity.status(status).body(errorBody);
    }
}
