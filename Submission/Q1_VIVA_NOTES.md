# CS336B Viva Questions & Answers: Question 1 (JDBC)

### Q1. Why use `PreparedStatement` instead of `Statement`?
**Answer**:
1. **Security (SQL Injection Prevention)**: `PreparedStatement` pre-compiles the SQL query template and treats parameters purely as literals (data), neutralizing SQL injection vectors like `' OR '1'='1`.
2. **Performance (Pre-compilation & Caching)**: The database parses, compiles, and optimizes the SQL template once. Repeated executions with different parameters reuse the cached query execution plan.
3. **Type Safety & Cleanliness**: Provides strongly-typed setters (`setInt()`, `setString()`, `setDouble()`), avoiding messy manual string concatenation and quote escapes.

---

### Q2. How is duplicate order ID handled in the program?
**Answer**:
1. **Pre-check validation**: An explicit lookup `SELECT 1 FROM Orders WHERE order_id = ?` can detect existing IDs before running the insert.
2. **Exception handling**: If an insert is attempted on a duplicate primary key, MySQL throws a `SQLIntegrityConstraintViolationException` (Vendor error code `1062`). The code catches this specific subclass of `SQLException`, prints an intuitive message to the user, and prevents program crash or unhandled termination.

---

### Q3. How does requirement (b) sorting work in MySQL?
**Answer**:
The query uses:
```sql
SELECT * FROM Orders WHERE LOWER(status) = LOWER(?) AND quantity > 2 ORDER BY unit_price DESC;
```
- Filtering happens in the `WHERE` clause before sorting.
- `ORDER BY unit_price DESC` arranges records from highest price to lowest price.
- `LOWER()` guarantees case-insensitive comparison regardless of database collation settings.

---

### Q4. What is the business logic behind requirement (e) deletion?
**Answer**:
In e-commerce systems, active orders (`Pending`, `Shipped`, `Delivered`) represent ongoing business commitments, financial accounting, and delivery tracking. Deleting active orders corrupts transaction history and audit trails. Therefore, records can only be deleted when their status is explicitly set to `Cancelled`.
