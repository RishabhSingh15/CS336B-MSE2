package com.example.LibraryManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CS336B - Object Oriented Programming with Java
 * Question 2: Main Spring Boot Application Entry Point
 */
@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
        System.out.println("\n=======================================================");
        System.out.println(" Library Management REST API Started Successfully!   ");
        System.out.println(" Base URL: http://localhost:8080/books                ");
        System.out.println("=======================================================\n");
    }
}
