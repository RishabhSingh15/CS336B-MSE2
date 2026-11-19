# Question 1: Order Management System (Java JDBC + MySQL)

## Course: CS336B - Object Oriented Programming with Java
**Examination**: Mid Semester Examination-II (ODD SEM-2026-27)  
**Competency / Outcome**: CO 4 (JDBC and Database Connectivity in Java)  
**Database**: `ecommerce_db`  
**Table**: `Orders`

---

## 1. Table Schema
The database contains a table named `Orders` with the following attributes:
| Column Name | Data Type | Constraint | Description |
| :--- | :--- | :--- | :--- |
| `order_id` | `INT` | `PRIMARY KEY` | Unique identifier for each order |
| `customer_name` | `VARCHAR(100)` | `NOT NULL` | Name of the customer placing order |
| `product_name` | `VARCHAR(100)` | `NOT NULL` | Name of the ordered item |
| `quantity` | `INT` | `NOT NULL, CHECK (quantity >= 0)` | Ordered quantity |
| `unit_price` | `DOUBLE` | `NOT NULL, CHECK (unit_price >= 0.0)` | Price per unit |
| `status` | `VARCHAR(30)` | `NOT NULL` | Order status (`Pending`, `Shipped`, `Delivered`, `Cancelled`) |

---

## 2. Requirements & Implementation Mapping

### **(a) Connection & Insert using PreparedStatement with Duplicate Handling**
- **Method**: `initializeDatabaseConnection()`, `insertSampleRecords()`, and `insertCustomOrder()`
- **SQL Query**:
  ```sql
  INSERT INTO Orders (order_id, customer_name, product_name, quantity, unit_price, status) 
  VALUES (?, ?, ?, ?, ?, ?);
  ```
- **Duplicate Handling**:
  - Catches `java.sql.SQLIntegrityConstraintViolationException` (MySQL Error Code 1062).
  - Also performs pre-emptive existence check (`SELECT 1 FROM Orders WHERE order_id = ?`).
  - Displays user-friendly error message rather than crashing the application.

### **(b) Filter Orders by Status & Quantity > 2 (Price DESC)**
- **Method**: `displayOrdersByStatusAndQuantity()`
- **SQL Query**:
  ```sql
  SELECT order_id, customer_name, product_name, quantity, unit_price, status 
  FROM Orders 
  WHERE LOWER(status) = LOWER(?) AND quantity > 2 
  ORDER BY unit_price DESC;
  ```
- Displays results formatted in an ASCII table with calculated subtotal (`quantity * unit_price`).

### **(c) Increase Existing Order Quantity**
- **Method**: `increaseOrderQuantity()`
- **Logic**:
  1. Checks if `order_id` exists. If not, outputs: `Order not found! No order exists with ID ...`.
  2. Accepts additional quantity and validates `additional_quantity > 0`.
  3. Executes:
     ```sql
     UPDATE Orders SET quantity = quantity + ? WHERE order_id = ?;
     ```
  4. Displays updated quantity confirmation.

### **(d) Validate & Update Order Status with Preview**
- **Method**: `updateOrderStatus()`
- **Valid Status Values**: `Pending`, `Shipped`, `Delivered`, `Cancelled`
- **Logic**:
  1. Retrieves current record via `SELECT ... WHERE order_id = ?`. If missing, shows appropriate error.
  2. Displays current details before updating.
  3. Validates new status input. If invalid, aborts update with explanatory message.
  4. Updates record:
     ```sql
     UPDATE Orders SET status = ? WHERE order_id = ?;
     ```
  5. Retrieves and displays the newly updated record.

### **(e) Total Order Value & Conditional Deletion**
- **Method 1 (Total Value)**: `displayTotalOrderValue()`
  - **SQL Query**:
    ```sql
    SELECT SUM(quantity * unit_price) AS total_val, COUNT(*) AS total_orders FROM Orders;
    ```
- **Method 2 (Delete Cancelled Order)**: `deleteCancelledOrder()`
  - **Logic**:
    1. Checks if order exists. If not, displays error.
    2. Checks if `status.equalsIgnoreCase("Cancelled")`.
    3. If status is NOT `Cancelled` (e.g. `Shipped` or `Pending`), rejects deletion with message:
       `"Cannot delete order ... Reason: Current status is '...'. Only orders with status 'Cancelled' can be deleted."`
    4. If `Cancelled`, executes:
       ```sql
       DELETE FROM Orders WHERE order_id = ?;
       ```

---

## 3. How to Run

### Option 1: Direct Command Line (Windows PowerShell / CMD)
```powershell
cd "c:\Users\Rishabh Singh\Pictures\MSE2\Question1"
javac -cp ".;lib\*" -d bin OrderManagementSystem.java
java -cp "bin;lib\*" question1.OrderManagementSystem
```

### Option 2: Run via Batch File or PowerShell Script
Double-click `run.bat` or run `.\run.ps1`.

### Option 3: Maven
```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="question1.OrderManagementSystem"
```
