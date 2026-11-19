-- ========================================================
-- CS336B - Object Oriented Programming with Java
-- Mid Semester Examination-II (ODD SEM-2026-27)
-- Question 1: Order Management System Database Schema
-- Database: ecommerce_db
-- Table: Orders
-- ========================================================

CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

DROP TABLE IF EXISTS Orders;

CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    unit_price DOUBLE NOT NULL CHECK (unit_price >= 0.0),
    status VARCHAR(30) NOT NULL
);

-- Sample initial records (at least three records for requirement a)
INSERT INTO Orders (order_id, customer_name, product_name, quantity, unit_price, status) VALUES
(101, 'Rahul Sharma', 'Wireless Mouse', 3, 599.00, 'Pending'),
(102, 'Priya Verma', 'Mechanical Keyboard', 1, 2499.50, 'Shipped'),
(103, 'Amit Patel', 'USB-C Fast Charger', 5, 899.00, 'Delivered'),
(104, 'Neha Gupta', 'Bluetooth Headphones', 4, 1899.00, 'Cancelled');

-- Verify inserted data
SELECT * FROM Orders;
