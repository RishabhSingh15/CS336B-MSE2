package com.example.LibraryManagement.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.LibraryManagement.model.Book;

/**
 * Service demonstrating in-memory storage using the Java Collections Framework.
 */
@Service
public class BookService {

    // Thread-safe collection for storing Book entities
    private final List<Book> bookList = new CopyOnWriteArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(100);

    public BookService() {
        // Initial generic educational sample books
        addSeedBook(new Book(1, "Object-Oriented Programming Fundamentals", "Alex Morgan", "Computer Science", 4));
        addSeedBook(new Book(2, "Database Systems and Architecture", "Jordan Lee", "Databases", 6));
        addSeedBook(new Book(3, "Modern Web Development Guide", "Sam Taylor", "Web Engineering", 3));
        addSeedBook(new Book(4, "Algorithms and Problem Solving", "Casey Rivera", "Algorithms", 5));
    }

    private void addSeedBook(Book book) {
        bookList.add(book);
        if (book.getId() != null && book.getId() >= idCounter.get()) {
            idCounter.set(book.getId() + 1);
        }
    }

    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(new ArrayList<>(bookList));
    }

    public Optional<Book> getBookById(Integer id) {
        if (id == null) return Optional.empty();
        return bookList.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    public Book addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book details cannot be empty.");
        }

        if (book.getId() == null || book.getId() <= 0) {
            book.setId(idCounter.getAndIncrement());
        } else {
            if (getBookById(book.getId()).isPresent()) {
                throw new IllegalStateException("Book with ID " + book.getId() + " already exists.");
            }
            if (book.getId() >= idCounter.get()) {
                idCounter.set(book.getId() + 1);
            }
        }

        bookList.add(book);
        return book;
    }

    public Optional<Book> updateBook(Integer id, Book updatedDetails) {
        if (id == null || updatedDetails == null) {
            return Optional.empty();
        }

        for (int i = 0; i < bookList.size(); i++) {
            Book existing = bookList.get(i);
            if (existing.getId().equals(id)) {
                existing.setTitle(updatedDetails.getTitle());
                existing.setAuthor(updatedDetails.getAuthor());
                existing.setCategory(updatedDetails.getCategory());
                existing.setAvailableCopies(updatedDetails.getAvailableCopies());
                return Optional.of(existing);
            }
        }
        return Optional.empty();
    }

    public boolean deleteBook(Integer id) {
        if (id == null) return false;
        return bookList.removeIf(b -> b.getId().equals(id));
    }
}
