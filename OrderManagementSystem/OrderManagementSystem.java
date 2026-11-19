// package ordermanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * CS336B - Object Oriented Programming with Java
 * Mid Semester Examination-II (ODD SEM-2026-27)
 * 
 * Question 1: Order Management System using Java JDBC and MySQL
 * Database: ecommerce_db
 * Table: Orders (order_id, customer_name, product_name, quantity, unit_price, status)
 *
 * Menu-driven application implementing:
 *  a) Database connection & inserting records using PreparedStatement with duplicate handling
 *  b) Displaying orders by status and quantity > 2 sorted by unit_price DESC
 *  c) Increasing existing order quantity by a given amount
 *  d) Updating order status with validation (Pending, Shipped, Delivered, Cancelled)
 *  e) Calculating total order value via SUM(quantity * unit_price) and conditional deletion
 */
public class OrderManagementSystem {

    // Database configuration
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "Rishabh@sql26";

    // Valid order statuses for requirement (d)
    private static final List<String> VALID_STATUSES = Arrays.asList(
            "Pending", "Shipped", "Delivered", "Cancelled"
    );

    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   CS336B: ORDER MANAGEMENT SYSTEM (Java JDBC + MySQL)         ");
        System.out.println("===============================================================");

        if (!initializeDatabaseConnection()) {
            System.err.println("Failed to establish database connection. Exiting application.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice (1-8): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Requirement (a): Insert 3 sample order records using PreparedStatement
                    insertSampleRecords();
                    break;
                case "2":
                    // Requirement (a): Insert custom order record with duplicate handling
                    insertCustomOrder();
                    break;
                case "3":
                    // Requirement (b): Filter by status and quantity > 2, sort unit_price DESC
                    displayOrdersByStatusAndQuantity();
                    break;
                case "4":
                    // Requirement (c): Increase existing order quantity
                    increaseOrderQuantity();
                    break;
                case "5":
                    // Requirement (d): Validate and update order status
                    updateOrderStatus();
                    break;
                case "6":
                    // Requirement (e): Display total order value using SUM(quantity * unit_price)
                    displayTotalOrderValue();
                    break;
                case "7":
                    // Requirement (e): Delete order only if status is 'Cancelled'
                    deleteCancelledOrder();
                    break;
                case "8":
                    // Helper option: Display all orders
                    displayAllOrders();
                    break;
                case "0":
                case "exit":
                case "quit":
                    running = false;
                    System.out.println("\nExiting Order Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please select an option between 1 and 8 (or 0 to exit).");
            }
            System.out.println();
        }

        closeConnection();
    }

    /**
     * Prints the interactive menu options.
     */
    private static void printMenu() {
        System.out.println("\n----------------------------- MENU ----------------------------");
        System.out.println("1. [Req a] Insert at least three sample orders (PreparedStatement)");
        System.out.println("2. [Req a] Insert a new order (with duplicate ID handling)");
        System.out.println("3. [Req b] Display orders by status & quantity > 2 (Price DESC)");
        System.out.println("4. [Req c] Increase existing order quantity");
        System.out.println("5. [Req d] Update order status (with validation & details preview)");
        System.out.println("6. [Req e] Display total order value [SUM(quantity * unit_price)]");
        System.out.println("7. [Req e] Delete order record (only if status is 'Cancelled')");
        System.out.println("8. Display all current orders in table");
        System.out.println("0. Exit");
        System.out.println("---------------------------------------------------------------");
    }

    /**
     * Requirement (a): Establishes the JDBC connection to MySQL database.
     */
    public static boolean initializeDatabaseConnection() {
        try {
            // Load driver (optional in JDBC 4.0+, but standard practice in academic Java)
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = System.getProperty("db.url", DEFAULT_URL);
            String user = System.getProperty("db.user", DEFAULT_USER);
            String pass = System.getProperty("db.password", DEFAULT_PASSWORD);

            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connection successfully established to: " + url.split("\\?")[0]);
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in classpath: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("SQLException while connecting to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Requirement (a):
     * Inserts at least three order records using PreparedStatement.
     * Handles duplicate order IDs gracefully.
     */
    public static void insertSampleRecords() {
        System.out.println("\n--- Inserting Sample Order Records (Requirement a) ---");

        Object[][] sampleData = {
                {201, "Aarav Sharma", "Sony Wireless Headphones", 3, 4999.00, "Pending"},
                {202, "Sneha Patel", "Logitech MX Master 3S Mouse", 1, 7995.00, "Shipped"},
                {203, "Rohan Verma", "Keychron Mechanical Keyboard", 4, 6499.00, "Delivered"},
                {204, "Ananya Iyer", "Dell 27-inch 4K Monitor", 2, 28999.00, "Pending"},
                {205, "Vikram Malhotra", "USB-C Hub Multiport Adapter", 5, 1499.00, "Cancelled"}
        };

        String sql = "INSERT INTO Orders (order_id, customer_name, product_name, quantity, unit_price, status) VALUES (?, ?, ?, ?, ?, ?)";

        int insertedCount = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Object[] row : sampleData) {
                int orderId = (int) row[0];
                String customer = (String) row[1];
                String product = (String) row[2];
                int qty = (int) row[3];
                double price = (double) row[4];
                String status = (String) row[5];

                pstmt.setInt(1, orderId);
                pstmt.setString(2, customer);
                pstmt.setString(3, product);
                pstmt.setInt(4, qty);
                pstmt.setDouble(5, price);
                pstmt.setString(6, status);

                try {
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.printf(" Successfully inserted Order ID: %d (%s)%n", orderId, product);
                        insertedCount++;
                    }
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.printf(" Skipping Order ID %d: Duplicate key exists (Order ID already present).%n", orderId);
                }
            }
            System.out.printf("Finished batch insertion. %d new records added.%n", insertedCount);
        } catch (SQLException e) {
            System.err.println("SQL Error during sample record insertion: " + e.getMessage());
        }
    }

    /**
     * Requirement (a):
     * Allows user to enter an order record interactively using PreparedStatement.
     * Explicitly checks and handles duplicate order IDs.
     */
    public static void insertCustomOrder() {
        System.out.println("\n--- Insert New Order (Requirement a) ---");
        try {
            System.out.print("Enter Order ID (Integer): ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());

            // Check if order ID already exists
            if (orderExists(orderId)) {
                System.out.printf(" Error: Duplicate Order ID! An order with ID %d already exists in the database.%n", orderId);
                return;
            }

            System.out.print("Enter Customer Name: ");
            String customerName = scanner.nextLine().trim();

            System.out.print("Enter Product Name: ");
            String productName = scanner.nextLine().trim();

            System.out.print("Enter Quantity (Integer): ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            if (quantity <= 0) {
                System.out.println(" Quantity must be greater than 0.");
                return;
            }

            System.out.print("Enter Unit Price (Double): ");
            double unitPrice = Double.parseDouble(scanner.nextLine().trim());
            if (unitPrice < 0) {
                System.out.println(" Unit price cannot be negative.");
                return;
            }

            System.out.print("Enter Status (Pending / Shipped / Delivered / Cancelled): ");
            String status = scanner.nextLine().trim();
            status = normalizeStatus(status);
            if (status == null) {
                System.out.println(" Invalid status! Allowed values: Pending, Shipped, Delivered, Cancelled.");
                return;
            }

            String sql = "INSERT INTO Orders (order_id, customer_name, product_name, quantity, unit_price, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, orderId);
                pstmt.setString(2, customerName);
                pstmt.setString(3, productName);
                pstmt.setInt(4, quantity);
                pstmt.setDouble(5, unitPrice);
                pstmt.setString(6, status);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.printf(" Order %d inserted successfully!%n", orderId);
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.printf(" Database constraint error: Duplicate order ID %d.%n", orderId);
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid numeric input. Please enter valid numbers for ID, quantity, and unit price.");
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    /**
     * Requirement (b):
     * Accepts an order status from the user and displays all orders with that status
     * and a quantity greater than 2. Results are sorted by unit_price in descending order.
     */
    public static void displayOrdersByStatusAndQuantity() {
        System.out.println("\n--- Filter Orders by Status & Quantity > 2 (Requirement b) ---");
        System.out.print("Enter Order Status to search (e.g. Pending, Shipped, Delivered, Cancelled): ");
        String searchStatus = scanner.nextLine().trim();

        String sql = "SELECT order_id, customer_name, product_name, quantity, unit_price, status "
                   + "FROM Orders "
                   + "WHERE LOWER(status) = LOWER(?) AND quantity > 2 "
                   + "ORDER BY unit_price DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, searchStatus);

            try (ResultSet rs = pstmt.executeQuery()) {
                printResultSet(rs, "Filtered Orders (Status='" + searchStatus + "' AND Quantity > 2, Ordered by unit_price DESC)");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while filtering orders: " + e.getMessage());
        }
    }

    /**
     * Requirement (c):
     * Accepts an order ID and an additional quantity.
     * Increases the existing quantity of the specified order by the given amount.
     * Displays an appropriate message if the order does not exist.
     */
    public static void increaseOrderQuantity() {
        System.out.println("\n--- Increase Order Quantity (Requirement c) ---");
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());

            // Check if order exists
            String checkSql = "SELECT order_id, product_name, quantity FROM Orders WHERE order_id = ?";
            int currentQty = -1;
            String prodName = "";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, orderId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        currentQty = rs.getInt("quantity");
                        prodName = rs.getString("product_name");
                    } else {
                        System.out.printf(" Order not found! No order exists with ID %d.%n", orderId);
                        return;
                    }
                }
            }

            System.out.printf("Current order found: ID=%d, Product='%s', Current Quantity=%d%n", orderId, prodName, currentQty);
            System.out.print("Enter additional quantity to add: ");
            int addQty = Integer.parseInt(scanner.nextLine().trim());

            if (addQty <= 0) {
                System.out.println(" Additional quantity must be greater than zero.");
                return;
            }

            String updateSql = "UPDATE Orders SET quantity = quantity + ? WHERE order_id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setInt(1, addQty);
                updateStmt.setInt(2, orderId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    int newQty = currentQty + addQty;
                    System.out.printf(" Successfully updated Order ID %d! Quantity increased from %d to %d (+%d).%n",
                            orderId, currentQty, newQty, addQty);
                } else {
                    System.out.printf(" Could not update order with ID %d.%n", orderId);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid number format entered.");
        } catch (SQLException e) {
            System.err.println("SQL Error while updating quantity: " + e.getMessage());
        }
    }

    /**
     * Requirement (d):
     * Accepts an order ID and a new status.
     * Retrieves the order details and updates the status ONLY IF the order exists
     * and the new status is valid (such as Pending, Shipped, or Delivered).
     * Displays the updated order details.
     */
    public static void updateOrderStatus() {
        System.out.println("\n--- Update Order Status (Requirement d) ---");
        try {
            System.out.print("Enter Order ID to update: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());

            // 1. Retrieve order details and check if it exists
            String selectSql = "SELECT order_id, customer_name, product_name, quantity, unit_price, status FROM Orders WHERE order_id = ?";
            String currentStatus = null;
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
                selectStmt.setInt(1, orderId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        currentStatus = rs.getString("status");
                        System.out.println("\nCurrent Order Details:");
                        System.out.printf("  Order ID      : %d%n", rs.getInt("order_id"));
                        System.out.printf("  Customer Name : %s%n", rs.getString("customer_name"));
                        System.out.printf("  Product Name  : %s%n", rs.getString("product_name"));
                        System.out.printf("  Quantity      : %d%n", rs.getInt("quantity"));
                        System.out.printf("  Unit Price    : $%.2f%n", rs.getDouble("unit_price"));
                        System.out.printf("  Current Status: %s%n", currentStatus);
                    } else {
                        System.out.printf(" Error: Order with ID %d does not exist!%n", orderId);
                        return;
                    }
                }
            }

            // 2. Accept and validate new status
            System.out.printf("Enter New Status %s: ", VALID_STATUSES);
            String rawStatus = scanner.nextLine().trim();
            String normalizedStatus = normalizeStatus(rawStatus);

            if (normalizedStatus == null) {
                System.out.printf(" Invalid status '%s'! Status must be one of: %s.%n", rawStatus, VALID_STATUSES);
                System.out.println("Update aborted.");
                return;
            }

            if (normalizedStatus.equalsIgnoreCase(currentStatus)) {
                System.out.printf(" The order is already in status '%s'. No change required.%n", currentStatus);
                return;
            }

            // 3. Update the status
            String updateSql = "UPDATE Orders SET status = ? WHERE order_id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setString(1, normalizedStatus);
                updateStmt.setInt(2, orderId);
                updateStmt.executeUpdate();
            }

            // 4. Retrieve and display the updated order details
            System.out.println("\n Status updated successfully! Updated Order Details:");
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
                selectStmt.setInt(1, orderId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.printf("  Order ID      : %d%n", rs.getInt("order_id"));
                        System.out.printf("  Customer Name : %s%n", rs.getString("customer_name"));
                        System.out.printf("  Product Name  : %s%n", rs.getString("product_name"));
                        System.out.printf("  Quantity      : %d%n", rs.getInt("quantity"));
                        System.out.printf("  Unit Price    : $%.2f%n", rs.getDouble("unit_price"));
                        System.out.printf("  Updated Status: %s%n", rs.getString("status"));
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid Order ID format.");
        } catch (SQLException e) {
            System.err.println("SQL Error while updating order status: " + e.getMessage());
        }
    }

    /**
     * Requirement (e):
     * Displays the total order value using SUM(quantity * unit_price).
     */
    public static void displayTotalOrderValue() {
        System.out.println("\n--- Total Order Value Calculation (Requirement e) ---");
        String sql = "SELECT SUM(quantity * unit_price) AS total_val, COUNT(*) AS total_orders FROM Orders";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                double totalVal = rs.getDouble("total_val");
                int totalOrders = rs.getInt("total_orders");
                System.out.println("-------------------------------------------------------");
                System.out.printf("Total Orders in System : %d%n", totalOrders);
                System.out.printf("Formula Applied        : SUM(quantity * unit_price)%n");
                System.out.printf("Total Order Value      : $%.2f%n", totalVal);
                System.out.println("-------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while calculating total order value: " + e.getMessage());
        }
    }

    /**
     * Requirement (e):
     * Accepts an order ID and deletes the corresponding record ONLY IF its status is Cancelled.
     * Displays an appropriate message if the order cannot be deleted.
     */
    public static void deleteCancelledOrder() {
        System.out.println("\n--- Delete Order Record (Requirement e) ---");
        try {
            System.out.print("Enter Order ID to delete: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());

            // 1. Check if the order exists and check its status
            String checkSql = "SELECT order_id, product_name, status FROM Orders WHERE order_id = ?";
            String status = null;
            String productName = null;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, orderId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        status = rs.getString("status");
                        productName = rs.getString("product_name");
                    } else {
                        System.out.printf(" Error: Order with ID %d cannot be deleted because it does not exist!%n", orderId);
                        return;
                    }
                }
            }

            // 2. Verify if status is 'Cancelled'
            if (!"Cancelled".equalsIgnoreCase(status)) {
                System.out.printf(" Deletion Rejected: Order ID %d (%s) cannot be deleted!%n", orderId, productName);
                System.out.printf(" Reason: Current status is '%s'. Only orders with status 'Cancelled' can be deleted.%n", status);
                return;
            }

            // 3. Perform deletion
            String deleteSql = "DELETE FROM Orders WHERE order_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, orderId);
                int rows = deleteStmt.executeUpdate();
                if (rows > 0) {
                    System.out.printf(" Successfully deleted cancelled Order ID %d (%s).%n", orderId, productName);
                } else {
                    System.out.printf(" Failed to delete Order ID %d.%n", orderId);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid Order ID format.");
        } catch (SQLException e) {
            System.err.println("SQL Error while deleting order: " + e.getMessage());
        }
    }

    /**
     * Helper to display all current orders in a formatted table.
     */
    public static void displayAllOrders() {
        String sql = "SELECT order_id, customer_name, product_name, quantity, unit_price, status FROM Orders ORDER BY order_id";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            printResultSet(rs, "All Orders in Database");
        } catch (SQLException e) {
            System.err.println("SQL Error while fetching all orders: " + e.getMessage());
        }
    }

    /**
     * Helper method to check if an order ID exists.
     */
    private static boolean orderExists(int orderId) throws SQLException {
        String sql = "SELECT 1 FROM Orders WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Normalizes and validates status string against VALID_STATUSES.
     */
    private static String normalizeStatus(String input) {
        if (input == null) return null;
        for (String valid : VALID_STATUSES) {
            if (valid.equalsIgnoreCase(input.trim())) {
                return valid;
            }
        }
        return null;
    }

    /**
     * Formats and prints a ResultSet into an ASCII table.
     */
    private static void printResultSet(ResultSet rs, String title) throws SQLException {
        System.out.println("\n=== " + title + " ===");
        System.out.printf("%-10s | %-20s | %-25s | %-8s | %-12s | %-12s | %-12s%n",
                "Order ID", "Customer Name", "Product Name", "Quantity", "Unit Price", "Status", "Subtotal");
        System.out.println("-------------------------------------------------------------------------------------------------------------");

        boolean hasData = false;
        while (rs.next()) {
            hasData = true;
            int id = rs.getInt("order_id");
            String customer = rs.getString("customer_name");
            String product = rs.getString("product_name");
            int qty = rs.getInt("quantity");
            double price = rs.getDouble("unit_price");
            String status = rs.getString("status");
            double subtotal = qty * price;

            System.out.printf("%-10d | %-20s | %-25s | %-8d | $%-11.2f | %-12s | $%-11.2f%n",
                    id, customer, product, qty, price, status, subtotal);
        }

        if (!hasData) {
            System.out.println("  (No matching records found)");
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Safely closes the database connection.
     */
    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed cleanly.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
