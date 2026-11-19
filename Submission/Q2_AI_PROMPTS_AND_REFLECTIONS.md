# Deliverable 2: AI Prompts and Reflections

## Prompt 1: Designing the Model and In-Memory Data Structure
- **Prompt**:
  "Design a Java model class named Book with fields id, title, author, category, and availableCopies. Provide appropriate constructors, getters, and setters, and explain how an in-memory collection like List or Map can be used safely in a Spring Boot service to store and manage these book records without an external database."
- **Reflection**:
  This prompt clarified the importance of choosing the right Java Collections Framework data structure. Using `CopyOnWriteArrayList` or synchronized collections ensures thread safety when concurrent HTTP requests access the collection, while standard getters and setters allow Spring Boot's Jackson serializer to convert JSON payloads to and from Java objects.

---

## Prompt 2: Developing RESTful Endpoints with Spring Annotations
- **Prompt**:
  "Write a Spring Boot RestController for a library management system with endpoints for adding a book via POST, retrieving books via GET (all and by ID), updating details via PUT, and deleting via DELETE. Include proper HTTP response status codes such as 200, 201, and 404."
- **Reflection**:
  This helped establish standard REST design principles. Using `@PostMapping` with `@RequestBody` deserializes incoming JSON payloads into model instances. Returning `ResponseEntity` allows full control over HTTP headers and status codes (such as `201 Created` for new resources and `404 Not Found` when a queried identifier does not exist).

---

## Prompt 3: Handling Edge Cases and Error Responses
- **Prompt**:
  "How should edge cases such as duplicate IDs on creation, non-existent IDs during update or deletion, and invalid numeric inputs be handled gracefully in a Spring Boot REST API?"
- **Reflection**:
  Handling edge cases makes REST services robust. By validating IDs and returning structured JSON error messages with clear HTTP status codes (`409 Conflict`, `404 Not Found`, `400 Bad Request`), client applications receive predictable error descriptions instead of generic stack traces.
