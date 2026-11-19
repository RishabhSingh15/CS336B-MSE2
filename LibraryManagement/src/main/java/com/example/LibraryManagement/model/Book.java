package com.example.LibraryManagement.model;

import java.util.Objects;

/**
 * CS336B - Object Oriented Programming with Java
 * Question 2, Part (a): Book Model Class
 *
 * Attributes:
 *  - id (Integer)
 *  - title (String)
 *  - author (String)
 *  - category (String)
 *  - availableCopies (Integer)
 */
public class Book {

    private Integer id;
    private String title;
    private String author;
    private String category;
    private Integer availableCopies;

    // Default No-Argument Constructor (Required for Jackson JSON deserialization)
    public Book() {
    }

    // Parameterized Constructor
    public Book(Integer id, String title, String author, String category, Integer availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availableCopies = availableCopies;
    }

    // --- Getters and Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    // --- Object Methods ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
