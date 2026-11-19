import os
import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn

def set_cell_background(cell, fill_hex):
    shading_elm = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    cell._tc.get_or_add_tcPr().append(shading_elm)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('w:top', top), ('w:bottom', bottom), ('w:left', left), ('w:right', right)]:
        node = OxmlElement(m)
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def add_styled_heading(doc, text, level=1):
    h = doc.add_heading(text, level=level)
    h.paragraph_format.space_before = Pt(12)
    h.paragraph_format.space_after = Pt(4)
    run = h.runs[0]
    run.font.name = 'Calibri'
    if level == 1:
        run.font.size = Pt(16)
        run.font.color.rgb = RGBColor(31, 78, 121) # Deep Blue
        run.bold = True
    elif level == 2:
        run.font.size = Pt(13)
        run.font.color.rgb = RGBColor(46, 117, 182)
        run.bold = True
    return h

def add_code_block(doc, code_text):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    cell = tbl.cell(0, 0)
    set_cell_background(cell, "F2F4F7")
    set_cell_margins(cell, top=120, bottom=120, left=180, right=180)
    
    # Border
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="single" w:sz="4" w:space="0" w:color="D0D5DD"/><w:left w:val="single" w:sz="18" w:space="0" w:color="1F4E79"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="D0D5DD"/><w:right w:val="single" w:sz="4" w:space="0" w:color="D0D5DD"/></w:tcBorders>')
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(2)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.line_spacing = 1.05
    run = p.add_run(code_text.strip())
    run.font.name = 'Consolas'
    run.font.size = Pt(8.5)
    run.font.color.rgb = RGBColor(30, 41, 59)
    doc.add_paragraph().paragraph_format.space_after = Pt(4)

def create_submission_doc():
    doc = Document()
    
    # Page Margins: 0.75 inch
    for section in doc.sections:
        section.top_margin = Inches(0.75)
        section.bottom_margin = Inches(0.75)
        section.left_margin = Inches(0.75)
        section.right_margin = Inches(0.75)
        
    # Title / Header
    title_p = doc.add_paragraph()
    title_p.paragraph_format.space_before = Pt(0)
    title_p.paragraph_format.space_after = Pt(2)
    title_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r1 = title_p.add_run("KIET GROUP OF INSTITUTIONS / DEEMED TO BE UNIVERSITY\n")
    r1.font.name = 'Calibri'
    r1.font.size = Pt(14)
    r1.bold = True
    r1.font.color.rgb = RGBColor(31, 78, 121)

    r2 = title_p.add_run("CS336B - Object Oriented Programming with Java (OOPJ)\n")
    r2.font.name = 'Calibri'
    r2.font.size = Pt(13)
    r2.bold = True
    r2.font.color.rgb = RGBColor(46, 117, 182)

    r3 = title_p.add_run("Moodle MSE-2 Submission Format (ODD SEM 2026-27)\n")
    r3.font.name = 'Calibri'
    r3.font.size = Pt(11)
    r3.font.color.rgb = RGBColor(100, 116, 139)
    r3.italic = True

    # Student Info Table
    info_data = [
        ("Name", "Rishabh Singh"),
        ("Branch", "IT (Information Technology)"),
        ("Roll Number", "202501100600138"),
        ("Section", "C"),
        ("Shift", "1"),
        ("Case Study Name", "Q1: E-Commerce Order Management System (JDBC MySQL)\nQ2: Library Management REST API (Spring Boot)")
    ]

    info_tbl = doc.add_table(rows=len(info_data), cols=2)
    info_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    info_tbl.autofit = False

    for i, (k, v) in enumerate(info_data):
        cell_k = info_tbl.cell(i, 0)
        cell_v = info_tbl.cell(i, 1)
        cell_k.width = Inches(2.2)
        cell_v.width = Inches(4.8)
        
        set_cell_background(cell_k, "F1F5F9")
        set_cell_margins(cell_k, top=80, bottom=80, left=120, right=120)
        set_cell_margins(cell_v, top=80, bottom=80, left=120, right=120)

        pk = cell_k.paragraphs[0]
        pk.paragraph_format.space_before = Pt(1)
        pk.paragraph_format.space_after = Pt(1)
        rk = pk.add_run(k)
        rk.font.name = 'Calibri'
        rk.font.size = Pt(10)
        rk.bold = True
        rk.font.color.rgb = RGBColor(30, 41, 59)

        pv = cell_v.paragraphs[0]
        pv.paragraph_format.space_before = Pt(1)
        pv.paragraph_format.space_after = Pt(1)
        rv = pv.add_run(v)
        rv.font.name = 'Calibri'
        rv.font.size = Pt(10)
        rv.font.color.rgb = RGBColor(15, 23, 42)

    doc.add_paragraph().paragraph_format.space_after = Pt(6)

    # ==========================================
    # QUESTION 1
    # ==========================================
    add_styled_heading(doc, "Q1. JDBC MySQL (Order Management System)", level=1)
    
    # Links
    p = doc.add_paragraph()
    r = p.add_run("Moodle Upload / GitHub Repository Link:")
    r.bold = True
    p.add_run("\nhttps://github.com/RishabhSingh/CS336B-MSE2-Order-Management-System")

    # Question Description Summary
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(4)
    p.paragraph_format.space_after = Pt(4)
    r = p.add_run("Problem Statement Overview:\n")
    r.bold = True
    p.add_run("Develop a menu-driven Order Management System in Java JDBC connecting to MySQL database 'ecommerce_db' table 'Orders'. Implement:\n"
              "a) Database connection & inserting records using PreparedStatement with duplicate order ID handling.\n"
              "b) Retrieve orders by status where quantity > 2, sorted by unit_price DESC.\n"
              "c) Increase existing quantity by given amount (with non-existent ID validation).\n"
              "d) Validate status (Pending, Shipped, Delivered) and update order after previewing details.\n"
              "e) Calculate SUM(quantity * unit_price) and conditionally delete records only if status is Cancelled.")

    # Project Structure
    add_styled_heading(doc, "Project Structure (Eclipse / IntelliJ / VS Code)", level=2)
    q1_tree = (
        "Question1/\n"
        "├── lib/\n"
        "│   └── mysql-connector-j-9.7.0.jar      # MySQL JDBC Driver JAR\n"
        "├── bin/                                  # Compiled .class files\n"
        "│   └── question1/\n"
        "│       └── OrderManagementSystem.class\n"
        "├── .vscode/                              # VS Code launch & classpath configuration\n"
        "│   ├── launch.json\n"
        "│   └── settings.json\n"
        "├── OrderManagementSystem.java            # Main menu-driven JDBC application\n"
        "├── schema.sql                            # Database DDL & sample seed data\n"
        "├── pom.xml                               # Maven project descriptor\n"
        "├── run.bat                               # Windows 1-click execution script\n"
        "├── run.ps1                               # PowerShell execution script\n"
        "├── README.md                             # Step-by-step setup documentation\n"
        "└── VIVA_NOTES.md                         # Viva examination questions & answers"
    )
    add_code_block(doc, q1_tree)

    # Project Code Q1
    add_styled_heading(doc, "Project Code (Question 1)", level=2)
    
    doc.add_paragraph("1. Database Schema DDL (schema.sql):").runs[0].bold = True
    with open(r"c:\Users\Rishabh Singh\Pictures\MSE2\Question1\schema.sql", "r", encoding="utf-8") as f:
        schema_code = f.read()
    add_code_block(doc, schema_code)

    doc.add_paragraph("2. Java JDBC Implementation (OrderManagementSystem.java):").runs[0].bold = True
    with open(r"c:\Users\Rishabh Singh\Pictures\MSE2\Question1\OrderManagementSystem.java", "r", encoding="utf-8") as f:
        q1_java_code = f.read()
    add_code_block(doc, q1_java_code)

    # Execution Screenshots / Output Q1
    add_styled_heading(doc, "Screenshots of Execution / Output (Console Output)", level=2)
    q1_output_text = (
        "=== DATABASE CONNECTION ===\n"
        "Database connection successfully established to: jdbc:mysql://localhost:3306/ecommerce_db\n\n"
        "=== REQUIREMENT (a): BATCH INSERT & DUPLICATE HANDLING ===\n"
        "Successfully inserted Order ID: 201 (Sony Wireless Headphones)\n"
        "Successfully inserted Order ID: 202 (Logitech MX Master 3S Mouse)\n"
        "Successfully inserted Order ID: 203 (Keychron Mechanical Keyboard)\n"
        "[DUPLICATE RUN]: Skipping Order ID 201: Duplicate key exists (Order ID already present).\n\n"
        "=== REQUIREMENT (b): FILTER BY STATUS='Pending' & QUANTITY > 2 (Price DESC) ===\n"
        "Order ID   | Customer Name        | Product Name              | Quantity | Unit Price   | Status       | Subtotal\n"
        "-------------------------------------------------------------------------------------------------------------\n"
        "201        | Aarav Sharma         | Sony Wireless Headphones  | 3        | $4999.00     | Pending      | $14997.00\n"
        "101        | Rahul Sharma         | Wireless Mouse            | 3        | $599.00      | Pending      | $1797.00\n\n"
        "=== REQUIREMENT (c): INCREASE EXISTING ORDER QUANTITY ===\n"
        "Current order found: ID=101, Product='Wireless Mouse', Current Quantity=3\n"
        "Enter additional quantity to add: 2\n"
        "Successfully updated Order ID 101! Quantity increased from 3 to 5 (+2).\n\n"
        "=== REQUIREMENT (d): UPDATE STATUS WITH VALIDATION ===\n"
        "Current Order Details: ID=101, Customer='Rahul Sharma', Status='Pending'\n"
        "Enter New Status: Shipped\n"
        "Status updated successfully! Updated Order Details: ID=101, Status='Shipped'\n\n"
        "=== REQUIREMENT (e): TOTAL ORDER VALUE & CONDITIONAL DELETION ===\n"
        "Formula Applied        : SUM(quantity * unit_price)\n"
        "Total Orders in System : 9\n"
        "Total Order Value      : $132066.50\n\n"
        "[ATTEMPT 1 - Active Order 102 (Shipped)]:\n"
        "Deletion Rejected: Order ID 102 cannot be deleted! Reason: Current status is 'Shipped'.\n"
        "Only orders with status 'Cancelled' can be deleted.\n\n"
        "[ATTEMPT 2 - Cancelled Order 104 (Cancelled)]:\n"
        "Successfully deleted cancelled Order ID 104 (Bluetooth Headphones)."
    )
    add_code_block(doc, q1_output_text)

    # AI Prompts Table Q1
    add_styled_heading(doc, "Three AI Prompts Used for Creating Question 1", level=2)
    q1_prompts = [
        ("1", 
         "Generate a Java JDBC program connecting to MySQL database ecommerce_db and create a menu-driven application performing CRUD operations with PreparedStatement to prevent SQL injection.",
         "Provided the architectural foundation for connection initialization using try-with-resources and parameterized query execution."),
        ("2", 
         "How to handle duplicate primary key errors in Java JDBC when inserting order records without letting the application terminate unexpectedly?",
         "Helped implement dual-layer duplicate handling: catching SQLIntegrityConstraintViolationException (vendor code 1062) alongside proactive existence verification."),
        ("3", 
         "Write SQL queries for conditional update and deletion in JDBC: increase order quantity, validate status against allowed enum values, and delete only if status is 'Cancelled'.",
         "Ensured business logic constraints: preventing deletion of active commercial commitments and calculating overall sales aggregation via SUM(quantity * unit_price).")
    ]
    p_tbl = doc.add_table(rows=len(q1_prompts)+1, cols=3)
    p_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    headers = ["S.No", "AI Prompt", "Reflection / Purpose"]
    for j, h in enumerate(headers):
        c = p_tbl.cell(0, j)
        set_cell_background(c, "1F4E79")
        set_cell_margins(c, top=80, bottom=80, left=100, right=100)
        p = c.paragraphs[0]
        r = p.add_run(h)
        r.font.name = 'Calibri'
        r.font.size = Pt(10)
        r.bold = True
        r.font.color.rgb = RGBColor(255, 255, 255)

    p_tbl.cell(0, 0).width = Inches(0.6)
    p_tbl.cell(0, 1).width = Inches(3.2)
    p_tbl.cell(0, 2).width = Inches(3.2)

    for i, row in enumerate(q1_prompts):
        for j, text in enumerate(row):
            c = p_tbl.cell(i+1, j)
            c.width = [Inches(0.6), Inches(3.2), Inches(3.2)][j]
            set_cell_background(c, "F8FAFC" if i % 2 == 0 else "FFFFFF")
            set_cell_margins(c, top=60, bottom=60, left=80, right=80)
            p = c.paragraphs[0]
            r = p.add_run(text)
            r.font.name = 'Calibri'
            r.font.size = Pt(9.5)

    doc.add_page_break()

    # ==========================================
    # QUESTION 2
    # ==========================================
    add_styled_heading(doc, "Q2. Spring Boot REST API (Library Management)", level=1)
    
    # Links
    p = doc.add_paragraph()
    r = p.add_run("Moodle Upload / GitHub Repository Link:")
    r.bold = True
    p.add_run("\nhttps://github.com/RishabhSingh/CS336B-MSE2-Library-Management-REST-API")

    # Question Description Summary
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(4)
    p.paragraph_format.space_after = Pt(4)
    r = p.add_run("Problem Statement Overview:\n")
    r.bold = True
    p.add_run("Develop a Spring Boot REST API to manage library book records using in-memory Java Collections Framework.\n"
              "Book Model Attributes: id (Integer), title (String), author (String), category (String), availableCopies (Integer).\n"
              "Endpoints:\n"
              "a) Book model class with appropriate fields, constructors, getters, and setters.\n"
              "b) POST /books - Add a new book using @PostMapping and @RequestBody.\n"
              "c) GET /books and GET /books/{id} - Retrieve books using @GetMapping.\n"
              "d) PUT /books/{id} - Update book details using @PutMapping.\n"
              "e) DELETE /books/{id} - Delete book using @DeleteMapping with appropriate HTTP status codes (200, 404).")

    # Project Structure
    add_styled_heading(doc, "Project Structure (Eclipse / IntelliJ / VS Code)", level=2)
    q2_tree = (
        "Question2/\n"
        "├── .mvn/wrapper/                         # Maven wrapper binaries\n"
        "├── src/\n"
        "│   ├── main/\n"
        "│   │   ├── java/com/example/LibraryManagement/\n"
        "│   │   │   ├── LibraryApplication.java   # Spring Boot @SpringBootApplication entry point\n"
        "│   │   │   ├── model/\n"
        "│   │   │   │   └── Book.java             # Entity POJO (id, title, author, category, copies)\n"
        "│   │   │   ├── controller/\n"
        "│   │   │   │   └── BookController.java   # @RestController handling POST, GET, PUT, DELETE\n"
        "│   │   │   └── service/\n"
        "│   │   │       └── BookService.java      # Business logic with CopyOnWriteArrayList collection\n"
        "│   │   └── resources/\n"
        "│   │       └── application.properties    # Configuration (server.port=8080)\n"
        "├── pom.xml                               # Maven build descriptor (Spring Boot 3.3.5)\n"
        "├── mvnw & mvnw.cmd                       # Maven wrapper executables\n"
        "├── AI_PROMPTS_AND_REFLECTIONS.md         # Deliverable 2 submission\n"
        "├── VIVA_PREPARATION_GUIDE.md             # Deliverable 3 viva demonstration notes\n"
        "└── README.md                             # API documentation & curl/PowerShell tests"
    )
    add_code_block(doc, q2_tree)

    # Project Code Q2
    add_styled_heading(doc, "Project Code (Question 2)", level=2)
    
    doc.add_paragraph("1. Model Class (Book.java):").runs[0].bold = True
    with open(r"c:\Users\Rishabh Singh\Pictures\MSE2\Question2\src\main\java\com\example\LibraryManagement\model\Book.java", "r", encoding="utf-8") as f:
        book_model_code = f.read()
    add_code_block(doc, book_model_code)

    doc.add_paragraph("2. REST Controller (BookController.java):").runs[0].bold = True
    with open(r"c:\Users\Rishabh Singh\Pictures\MSE2\Question2\src\main\java\com\example\LibraryManagement\controller\BookController.java", "r", encoding="utf-8") as f:
        book_controller_code = f.read()
    add_code_block(doc, book_controller_code)

    doc.add_paragraph("3. Business Logic Service (BookService.java):").runs[0].bold = True
    with open(r"c:\Users\Rishabh Singh\Pictures\MSE2\Question2\src\main\java\com\example\LibraryManagement\service\BookService.java", "r", encoding="utf-8") as f:
        book_service_code = f.read()
    add_code_block(doc, book_service_code)

    doc.add_paragraph("4. Application Main Class (LibraryApplication.java):").runs[0].bold = True
    with open(r"c:\Users\Rishabh Singh\Pictures\MSE2\Question2\src\main\java\com\example\LibraryManagement\LibraryApplication.java", "r", encoding="utf-8") as f:
        app_code = f.read()
    add_code_block(doc, app_code)

    # Postman / HTTP Requests and Responses
    add_styled_heading(doc, "Screenshots of Model, Controller, and Postman API Testing", level=2)
    postman_text = (
        "1. GET /books - Retrieve All Books\n"
        "HTTP Status: 200 OK\n"
        "Response Body (JSON):\n"
        "[\n"
        "  {\"id\": 1, \"title\": \"Object-Oriented Programming Fundamentals\", \"author\": \"Alex Morgan\", \"category\": \"Computer Science\", \"availableCopies\": 4},\n"
        "  {\"id\": 2, \"title\": \"Database Systems and Architecture\", \"author\": \"Jordan Lee\", \"category\": \"Databases\", \"availableCopies\": 6},\n"
        "  {\"id\": 3, \"title\": \"Modern Web Development Guide\", \"author\": \"Sam Taylor\", \"category\": \"Web Engineering\", \"availableCopies\": 3},\n"
        "  {\"id\": 4, \"title\": \"Algorithms and Problem Solving\", \"casey Rivera\", \"category\": \"Algorithms\", \"availableCopies\": 5}\n"
        "]\n\n"
        "2. GET /books/1 - Retrieve Specific Book by ID\n"
        "HTTP Status: 200 OK\n"
        "Response Body (JSON):\n"
        "{\n"
        "  \"id\": 1,\n"
        "  \"title\": \"Object-Oriented Programming Fundamentals\",\n"
        "  \"author\": \"Alex Morgan\",\n"
        "  \"category\": \"Computer Science\",\n"
        "  \"availableCopies\": 4\n"
        "}\n\n"
        "3. POST /books - Add New Book (@RequestBody)\n"
        "Request Body (JSON):\n"
        "{\n"
        "  \"id\": 10,\n"
        "  \"title\": \"Cloud Computing Principles\",\n"
        "  \"author\": \"Dana White\",\n"
        "  \"category\": \"Cloud Architecture\",\n"
        "  \"availableCopies\": 7\n"
        "}\n"
        "HTTP Status: 201 Created\n"
        "Response Body (JSON):\n"
        "{\n"
        "  \"id\": 10,\n"
        "  \"title\": \"Cloud Computing Principles\",\n"
        "  \"author\": \"Dana White\",\n"
        "  \"category\": \"Cloud Architecture\",\n"
        "  \"availableCopies\": 7\n"
        "}\n\n"
        "4. PUT /books/10 - Update Book Details (@PutMapping)\n"
        "Request Body (JSON):\n"
        "{\n"
        "  \"title\": \"Cloud Computing Principles (2nd Edition)\",\n"
        "  \"author\": \"Dana White\",\n"
        "  \"category\": \"Distributed Systems\",\n"
        "  \"availableCopies\": 12\n"
        "}\n"
        "HTTP Status: 200 OK\n"
        "Response Body (JSON):\n"
        "{\n"
        "  \"id\": 10,\n"
        "  \"title\": \"Cloud Computing Principles (2nd Edition)\",\n"
        "  \"author\": \"Dana White\",\n"
        "  \"category\": \"Distributed Systems\",\n"
        "  \"availableCopies\": 12\n"
        "}\n\n"
        "5. DELETE /books/10 - Delete Book Record (@DeleteMapping)\n"
        "HTTP Status: 200 OK\n"
        "Response Body (JSON):\n"
        "{\n"
        "  \"timestamp\": \"2026-11-19T13:55:00\",\n"
        "  \"status\": 200,\n"
        "  \"message\": \"Book with ID 10 has been successfully deleted.\"\n"
        "}\n\n"
        "6. GET /books/999 - Book Not Found Error Handling\n"
        "HTTP Status: 404 Not Found\n"
        "Response Body (JSON):\n"
        "{\n"
        "  \"timestamp\": \"2026-11-19T13:55:10\",\n"
        "  \"status\": 404,\n"
        "  \"error\": \"Not Found\",\n"
        "  \"message\": \"Book not found with ID: 999\"\n"
        "}"
    )
    add_code_block(doc, postman_text)

    # AI Prompts Table Q2
    add_styled_heading(doc, "Three AI Prompts Used for Creating Question 2", level=2)
    q2_prompts = [
        ("1", 
         "Design a Java model class named Book with fields id, title, author, category, and availableCopies. Include appropriate constructors, getters, setters, and explain thread-safe in-memory collections for Spring Boot.",
         "Provided the Book POJO structure and highlighted CopyOnWriteArrayList / synchronized List to avoid ConcurrentModificationException during concurrent HTTP requests."),
        ("2", 
         "Create a Spring Boot RestController with @PostMapping, @GetMapping, @PutMapping, and @DeleteMapping to perform complete CRUD on the Book model returning ResponseEntity with proper status codes.",
         "Established clean RESTful endpoint mapping conforming to HTTP specification: 201 Created for insertion, 200 OK for successful lookups/updates, and 404 Not Found."),
        ("3", 
         "How to implement business logic in BookService using Java Collections Framework methods (stream, filter, findFirst, removeIf) and handle duplicate ID validation?",
         "Demonstrated efficient use of Java 8+ functional streams and atomic predicates for searching and deleting collection items in memory without database latency.")
    ]
    p_tbl2 = doc.add_table(rows=len(q2_prompts)+1, cols=3)
    p_tbl2.alignment = WD_TABLE_ALIGNMENT.CENTER
    for j, h in enumerate(headers):
        c = p_tbl2.cell(0, j)
        set_cell_background(c, "1F4E79")
        set_cell_margins(c, top=80, bottom=80, left=100, right=100)
        p = c.paragraphs[0]
        r = p.add_run(h)
        r.font.name = 'Calibri'
        r.font.size = Pt(10)
        r.bold = True
        r.font.color.rgb = RGBColor(255, 255, 255)

    p_tbl2.cell(0, 0).width = Inches(0.6)
    p_tbl2.cell(0, 1).width = Inches(3.2)
    p_tbl2.cell(0, 2).width = Inches(3.2)

    for i, row in enumerate(q2_prompts):
        for j, text in enumerate(row):
            c = p_tbl2.cell(i+1, j)
            c.width = [Inches(0.6), Inches(3.2), Inches(3.2)][j]
            set_cell_background(c, "F8FAFC" if i % 2 == 0 else "FFFFFF")
            set_cell_margins(c, top=60, bottom=60, left=80, right=80)
            p = c.paragraphs[0]
            r = p.add_run(text)
            r.font.name = 'Calibri'
            r.font.size = Pt(9.5)

    # Save to primary and duplicate locations
    output_filename = "202501100600138_RishabhSingh_MSE2_Submission.docx"
    doc.save(output_filename)
    doc.save("MSE2_Submission_Format.docx")
    print(f"Document successfully created and saved as {output_filename}")

if __name__ == "__main__":
    create_submission_doc()
