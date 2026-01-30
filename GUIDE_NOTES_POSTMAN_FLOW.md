# GUIDE: Postman API Flow with Code-Level Explanation

> **For:** 7th Standard Students Learning Java + Spring Boot  
> **Goal:** Understand exactly what code runs when you click "Send" in Postman

---

# Table of Contents

## REST APIs
1. [Create Student (POST)](#1-create-student-post)
2. [Get Student by ID (GET)](#2-get-student-by-id-get)
3. [Get Student by ID - Not Found (GET)](#3-get-student-by-id---not-found-get)
4. [Health Check (GET)](#4-health-check-get)

## SOAP APIs
5. [Create Student (SOAP)](#5-create-student-soap)
6. [Get Student by ID (SOAP)](#6-get-student-by-id-soap)
7. [Get WSDL](#7-get-wsdl)

## GraphQL APIs
8. [Create Student (Mutation)](#8-create-student-mutation)
9. [Get Student by ID (Query)](#9-get-student-by-id-query)
10. [Get Student - Only Name (Selective)](#10-get-student---only-name-selective)

## Utilities
11. [H2 Console (Browser)](#11-h2-console-browser)
12. [GraphiQL Playground (Browser)](#12-graphiql-playground-browser)

---

# REST APIs

---

## 1) Create Student (POST)

### 1) What you send (Request)

- **Method:** POST
- **URL:** `http://localhost:8080/api/students`
- **Headers:** 
  - `Content-Type: application/json`
- **Body:**

```json
{
    "name": "John Doe",
    "age": 15,
    "grade": "A"
}
```

### 2) What you get back (Expected Response)

- **Status:** 201 Created
- **Response example:**

```json
{
    "id": 1,
    "name": "John Doe",
    "age": 15,
    "grade": "A"
}
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman 
  ↓
Tomcat Server (receives HTTP request)
  ↓
DispatcherServlet (Spring's traffic controller)
  ↓
StudentController.createStudent() [REST Layer]
  ↓
StudentService.createStudent() [Business Logic Layer]
  ↓
StudentRepository.save() [Data Access Layer]
  ↓
JPA/Hibernate (converts Java → SQL)
  ↓
H2 Database (INSERT query executes)
  ↓
Response travels back up the same path
  ↓
JSON response sent to Postman
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: Request enters Tomcat server**

```
Tomcat (embedded server in Spring Boot) receives:
POST http://localhost:8080/api/students
Content-Type: application/json
Body: {"name":"John Doe","age":15,"grade":"A"}
```

**What happens:** Tomcat is a web server inside your Spring Boot app. When you click "Send" in Postman, the request travels through the internet and reaches Tomcat on port 8080.

---

#### **Step 2: DispatcherServlet finds the right controller**

```java
// This is Spring's internal class (you don't write this)
// Located in: org.springframework.web.servlet.DispatcherServlet
// It looks at URL path and method to find matching @Controller
```

**What happens:** DispatcherServlet is like a receptionist. It reads the URL `/api/students` and method `POST`, then searches for a controller with matching `@PostMapping`.

---

#### **Step 3: StudentController method runs**

```java
// File: StudentController.java

// Line 1: @RestController - Tells Spring this class handles web requests
// This combines @Controller + @ResponseBody
// Meaning: Methods return data (not HTML pages), converted to JSON automatically
@RestController

// Line 2: @RequestMapping - Sets base URL for all methods in this class
// All methods here start with /api/students
// Example: /api/students, /api/students/1, /api/students/health
@RequestMapping("/api/students")

// Line 3: public class - Declares a public class (visible to Spring)
public class StudentController {

    // Line 4: private final - Declares a variable that:
    // - private = only this class can use it
    // - final = cannot be changed after set (like a constant reference)
    // StudentService = type of object we're storing
    private final StudentService studentService;
    
    // Line 5-7: Constructor - Special method called when object is created
    // Spring calls this automatically and provides StudentService
    // This is called "Constructor Injection" (Spring injects dependency)
    public StudentController(StudentService studentService) {
        // Line 6: this.studentService = stores the provided service
        // "this" means "this class's variable"
        this.studentService = studentService;
    }

    // Line 8: @PostMapping - Matches HTTP POST requests
    // When someone does: POST /api/students, this method runs
    @PostMapping
    
    // Line 9: Method declaration
    // - public = anyone can call it
    // - ResponseEntity<StudentResponse> = return type (HTTP response with StudentResponse body)
    // - createStudent = method name
    // - @RequestBody StudentRequest request = parameter:
    //   * @RequestBody tells Spring: "Take JSON from request body"
    //   * Spring converts JSON → StudentRequest object automatically
    //   * request = variable name to use in this method
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest request) {
        
        // Line 10: Call service layer to do the actual work
        // studentService.createStudent(request) returns StudentResponse
        // We store result in "response" variable
        StudentResponse response = studentService.createStudent(request);
        
        // Line 11: Return HTTP response
        // ResponseEntity.status(HttpStatus.CREATED) = Set status to 201 Created
        // .body(response) = Put StudentResponse object in response body
        // Spring converts StudentResponse → JSON automatically
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

**What happens:**
- Spring converts JSON → `StudentRequest` object automatically (this is called **deserialization**)
- `@RequestBody` tells Spring: "Take JSON from request body and convert it to StudentRequest object"
- Controller calls service layer (`studentService.createStudent(request)`)
- Returns `ResponseEntity` with status 201 and StudentResponse object

---

#### **Step 4: StudentService processes business logic**

```java
// File: StudentService.java

// Line 1: @Service - Tells Spring this is a service layer class
// Service = Contains business logic (rules, calculations, coordination)
// Spring creates ONE instance and manages it
@Service

// Line 2: Declare public class
public class StudentService {

    // Line 3: Declare repository variable
    // private final = Cannot change after constructor sets it
    // StudentRepository = Interface for database operations
    private final StudentRepository studentRepository;
    
    // Line 4-6: Constructor - Spring calls this and injects repository
    public StudentService(StudentRepository studentRepository) {
        // Line 5: Store the repository so we can use it later
        this.studentRepository = studentRepository;
    }

    // Line 7: Method to create student
    // Input: StudentRequest (DTO from controller)
    // Output: StudentResponse (DTO back to controller)
    public StudentResponse createStudent(StudentRequest request) {
        
        // Step 4a: Create new Student entity from request DTO
        // Line 8: new Student() creates empty Student object
        Student student = new Student();
        
        // Line 9: Copy name from request to student entity
        // request.getName() gets "John Doe" from request
        // student.setName(...) stores it in entity
        student.setName(request.getName());  // "John Doe"
        
        // Line 10: Copy age from request (15)
        student.setAge(request.getAge());    // 15
        
        // Line 11: Copy grade from request ("A")
        student.setGrade(request.getGrade()); // "A"
        
        // Step 4b: Save to database using repository
        // Line 12: studentRepository.save(student) does:
        //   1. JPA converts Student object → SQL INSERT
        //   2. Database executes INSERT
        //   3. Database generates ID (1, 2, 3...)
        //   4. Returns Student object with ID filled in
        // We store result in savedStudent variable
        Student savedStudent = studentRepository.save(student);
        
        // Step 4c: Convert entity to response DTO
        // Line 13: Call helper method to convert Student → StudentResponse
        // Then return that response to controller
        return convertToResponse(savedStudent);
    }
    
    // Line 14: Helper method to convert Entity → DTO
    // private = only this class can call it
    // Input: Student entity (from database)
    // Output: StudentResponse DTO (for controller)
    private StudentResponse convertToResponse(Student student) {
        
        // Line 15: Create empty StudentResponse object
        StudentResponse response = new StudentResponse();
        
        // Line 16: Copy ID from entity to response
        // student.getId() gets database-generated ID (like 1)
        // response.setId(...) stores it in DTO
        response.setId(student.getId());       // Database generated ID
        
        // Line 17: Copy name from entity to response
        response.setName(student.getName());
        
        // Line 18: Copy age from entity to response
        response.setAge(student.getAge());
        
        // Line 19: Copy grade from entity to response
        response.setGrade(student.getGrade());
        
        // Line 20: Return the complete response DTO
        return response;
    }
}
```

**What happens:**
- Service receives `StudentRequest` (DTO = Data Transfer Object)
- Creates `Student` entity (JPA entity = database table row)
- Calls repository to save
- Converts saved entity → `StudentResponse` DTO
- **Why DTO?** Controllers and services exchange DTOs (not entities) for security and flexibility

---

#### **Step 5: StudentRepository saves to database**

```java
// File: StudentRepository.java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // No methods needed! JPA provides save(), findById(), etc. automatically
}
```

**What happens:**
- This is just an **interface** (no implementation needed!)
- Spring Data JPA automatically implements this at runtime
- When you call `repository.save(student)`, JPA creates the implementation

---

#### **Step 6: JPA/Hibernate generates SQL**

```java
// File: Student.java (Entity)

// Line 1: @Entity - Tells JPA: "This class represents a database table"
// JPA will create/manage a table for this class
@Entity

// Line 2: @Table - Specifies table name in database
// name = "students" means table is called "students" (not "Student")
// Without this, table name would be "student" (lowercase class name)
@Table(name = "students")

// Line 3: Declare public class
public class Student {
    
    // Line 4: @Id - Marks this field as the Primary Key
    // Primary Key = unique identifier for each row (no duplicates allowed)
    @Id
    
    // Line 5: @GeneratedValue - Database auto-generates this value
    // strategy = GenerationType.IDENTITY means:
    //   - Database decides the ID (usually 1, 2, 3, 4...)
    //   - We don't set it manually
    //   - Database guarantees it's unique
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    // Line 6: Field to store ID
    // private = only this class accesses directly (use getters/setters)
    // Long = data type (whole numbers, can be null)
    // id = variable name
    private Long id;
    
    // Line 7: Field to store student name
    // String = text data type
    // JPA creates column "name" in database table
    private String name;
    
    // Line 8: Field to store student age
    // Integer = whole number data type (can be null)
    // JPA creates column "age" in database table
    private Integer age;
    
    // Line 9: Field to store student grade
    // JPA creates column "grade" in database table
    private String grade;
    
    // Constructors, getters, setters...
    // (Not shown but required for JPA to work)
    // - Default constructor: Student() - creates empty object
    // - Parameterized constructor: Student(name, age, grade)
    // - Getters: getId(), getName(), getAge(), getGrade()
    // - Setters: setId(), setName(), setAge(), setGrade()
}
```

**What happens:** Hibernate (JPA implementation) looks at `@Entity` class and generates SQL:

```sql
-- When you call repository.save(student), JPA generates this SQL:
INSERT INTO students (name, age, grade) 
VALUES ('John Doe', 15, 'A');

-- Note: ID is NOT in INSERT because database auto-generates it
-- Database automatically adds id = 1 (or next available number)
```

**How ID is generated:**
- `@GeneratedValue` tells database: "You generate the ID"
- Database auto-generates ID = 1 (first student), 2 (second), etc.
- After INSERT, database returns the generated ID
- JPA updates the Student object with this ID

---

#### **Step 7: H2 Database executes INSERT**

```
H2 Database receives SQL:
INSERT INTO students (name, age, grade) VALUES ('John Doe', 15, 'A');

Database executes → New row inserted:
| id | name     | age | grade |
|----|----------|-----|-------|
| 1  | John Doe | 15  | A     |

Database returns generated ID: 1
```

**What happens:** H2 is an in-memory database (stored in RAM, not disk). It creates a row and returns the auto-generated ID.

---

#### **Step 8: Response travels back up**

```
H2 returns: Student{id=1, name="John Doe", age=15, grade="A"}
  ↓
Hibernate converts SQL result → Student entity object
  ↓
StudentService converts Student entity → StudentResponse DTO
  ↓
StudentController wraps in ResponseEntity with status 201
  ↓
Spring converts StudentResponse → JSON (this is called serialization)
  ↓
Tomcat sends HTTP response to Postman
```

**Final Response:**
```
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "name": "John Doe",
    "age": 15,
    "grade": "A"
}
```

---

### 5) "Put breakpoint here" (Debug learning)

**Where to put breakpoints in your IDE:**

1. **Line in Controller:** `StudentResponse response = studentService.createStudent(request);`
   - See the incoming request object
   - Check if JSON converted to StudentRequest correctly

2. **Line in Service:** `Student savedStudent = studentRepository.save(student);`
   - See Student entity before saving
   - Check values are set correctly

3. **Line in Service (after save):** `return convertToResponse(savedStudent);`
   - See auto-generated ID after database insert
   - Verify database returned the saved student

4. **Line in Controller:** `return ResponseEntity.status(HttpStatus.CREATED).body(response);`
   - See final response before sending to Postman
   - Verify HTTP status is 201

5. **Inside convertToResponse():** `response.setId(student.getId());`
   - Check if entity → DTO conversion works

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **@RestController** | Tells Spring: "This class handles web requests and returns JSON" |
| **@PostMapping** | Matches POST HTTP method to this Java method |
| **@RequestBody** | Converts JSON from request → Java object |
| **@Service** | Marks a class as business logic layer |
| **@Repository** | Marks an interface as database access layer |
| **@Entity** | Marks a class as database table |
| **DTO (Data Transfer Object)** | Objects used to transfer data between layers (StudentRequest, StudentResponse) |
| **Entity** | Objects that represent database table rows (Student) |
| **JPA (Java Persistence API)** | Java standard for database operations (Hibernate implements it) |
| **Interface** | A contract (StudentRepository) - Spring implements it automatically |
| **Dependency Injection** | Spring automatically provides objects when constructor is called |

---

## 2) Get Student by ID (GET)

### 1) What you send (Request)

- **Method:** GET
- **URL:** `http://localhost:8080/api/students/1`
- **Headers:** None
- **Body:** None (GET requests don't have body)

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example:**

```json
{
    "id": 1,
    "name": "John Doe",
    "age": 15,
    "grade": "A"
}
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman 
  ↓
Tomcat Server
  ↓
DispatcherServlet
  ↓
StudentController.getStudentById(Long id) [REST Layer]
  ↓
StudentService.getStudentById(Long id) [Business Logic]
  ↓
StudentRepository.findById(Long id) [Data Access]
  ↓
JPA/Hibernate generates SELECT query
  ↓
H2 Database executes query
  ↓
Response travels back up
  ↓
JSON response to Postman
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: Request enters Tomcat**

```
GET http://localhost:8080/api/students/1
No body (GET requests don't have body)
```

**What happens:** Tomcat receives GET request with ID in URL path.

---

#### **Step 2: DispatcherServlet extracts path variable**

```java
// Spring extracts "1" from URL path /api/students/1
// This "1" becomes the method parameter
```

**What happens:** DispatcherServlet sees `/api/students/1` and extracts `1` as the ID parameter.

---

#### **Step 3: StudentController method runs**

```java
// File: StudentController.java
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    
    // Line 1: @GetMapping - Matches HTTP GET requests
    // "/{id}" means URL has a variable part
    // Examples: /api/students/1, /api/students/5, /api/students/999
    // {id} is a placeholder that captures the number from URL
    @GetMapping("/{id}")
    
    // Line 2: Method declaration
    // - public = anyone can call
    // - ResponseEntity<StudentResponse> = return type (HTTP response with body)
    // - getStudentById = method name
    // - @PathVariable Long id = parameter:
    //   * @PathVariable tells Spring: "Extract {id} from URL path"
    //   * Spring automatically converts it to Long (number)
    //   * Example: URL /api/students/1 → id = 1L
    //   * Example: URL /api/students/999 → id = 999L
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        
        // Line 3: Call service layer to get student from database
        // Pass the ID from URL to service
        // Service returns StudentResponse or throws exception if not found
        StudentResponse response = studentService.getStudentById(id);
        
        // Line 4: Return HTTP 200 OK response
        // ResponseEntity.ok(response) creates:
        //   - Status: 200 OK
        //   - Body: StudentResponse converted to JSON
        return ResponseEntity.ok(response);
    }
}
```

**What happens:**
- `@GetMapping("/{id}")` matches GET requests like `/api/students/1`, `/api/students/2`, etc.
- `@PathVariable` tells Spring: "Extract {id} from URL and convert to Long"
- URL `/api/students/1` → method parameter `id = 1L`
- Calls service method with ID
- Returns 200 OK with StudentResponse

---

#### **Step 4: StudentService retrieves from database**

```java
// File: StudentService.java
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    
    // Line 1: Method to get student by ID
    // Input: Long id (from controller)
    // Output: StudentResponse (DTO) or throws exception
    public StudentResponse getStudentById(Long id) {
        
        // Step 4a: Try to find student in database
        
        // Line 2: Call repository to search database
        // studentRepository.findById(id) returns Optional<Student>
        // Optional = a box that either:
        //   - Contains Student (if found)
        //   - Is empty (if not found)
        // This prevents null pointer errors!
        
        // Line 3: .orElseThrow() means:
        // "If Optional is empty (student not found), throw exception"
        // () -> new StudentNotFoundException(...) is a lambda expression
        // Lambda = short way to create an exception when needed
        // If student IS found, this line is skipped and we get the Student object
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        
        // Step 4b: If we reach here, student was found!
        // Line 4: Convert Student entity → StudentResponse DTO
        // Then return to controller
        return convertToResponse(student);
    }
    
    // Helper method (same as before)
    private StudentResponse convertToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setAge(student.getAge());
        response.setGrade(student.getGrade());
        return response;
    }
}
```

**What happens:**
- `findById(id)` returns `Optional<Student>` (a box that may or may not contain a student)
- If student exists → returns Student object
- If student doesn't exist → throws `StudentNotFoundException`
- `.orElseThrow(...)` means: "If Optional is empty, throw exception"

---

#### **Step 5: StudentRepository queries database**

```java
// File: StudentRepository.java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // findById(Long id) is provided by JpaRepository automatically
}
```

**What happens:**
- `findById(id)` is inherited from `JpaRepository`
- JPA automatically implements this method
- Returns `Optional<Student>` (empty if not found)

---

#### **Step 6: JPA/Hibernate generates SELECT query**

```sql
SELECT * FROM students WHERE id = 1;
```

**What happens:** Hibernate generates SQL query to fetch student with ID = 1.

---

#### **Step 7: H2 Database executes SELECT**

```
H2 Database receives:
SELECT * FROM students WHERE id = 1;

Database searches for row with id = 1:
| id | name     | age | grade |
|----|----------|-----|-------|
| 1  | John Doe | 15  | A     |  ← Found!

Returns this row to Hibernate
```

**What happens:** Database finds the row and returns it.

---

#### **Step 8: Response travels back up**

```
H2 returns row data
  ↓
Hibernate converts SQL result → Student entity object
  ↓
StudentService converts Student entity → StudentResponse DTO
  ↓
StudentController wraps in ResponseEntity (200 OK)
  ↓
Spring converts StudentResponse → JSON
  ↓
Tomcat sends to Postman
```

**Final Response:**
```
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "name": "John Doe",
    "age": 15,
    "grade": "A"
}
```

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in Controller:** `StudentResponse response = studentService.getStudentById(id);`
   - Check if ID extracted from URL correctly

2. **Line in Service:** `Student student = studentRepository.findById(id)`
   - See what ID is being searched

3. **Line in Service (after findById):** `.orElseThrow(...)`
   - Check if student was found or Optional is empty

4. **Line in Service:** `return convertToResponse(student);`
   - See Student entity before converting to DTO

5. **Line in Controller:** `return ResponseEntity.ok(response);`
   - Verify final response

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **@GetMapping** | Matches GET HTTP method to Java method |
| **@PathVariable** | Extracts value from URL path (e.g., `/students/1` → `id=1`) |
| **Optional<T>** | A box that may contain a value or be empty (avoids null pointer errors) |
| **.orElseThrow()** | If Optional is empty, throw exception; otherwise return value |
| **Lambda expression** | `() -> new Exception()` is a short way to create objects |
| **Exception** | When error happens, throw exception to stop normal flow |

---

## 3) Get Student by ID - Not Found (GET)

### 1) What you send (Request)

- **Method:** GET
- **URL:** `http://localhost:8080/api/students/999`
- **Headers:** None
- **Body:** None

### 2) What you get back (Expected Response)

- **Status:** 404 Not Found
- **Response example:**

```json
{
    "timestamp": "2026-01-30T10:15:30",
    "status": 404,
    "error": "Not Found",
    "message": "Student not found with id: 999",
    "path": "/api/students/999"
}
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman 
  ↓
Tomcat → DispatcherServlet → StudentController
  ↓
StudentService.getStudentById(999)
  ↓
StudentRepository.findById(999)
  ↓
H2 Database (No row found!)
  ↓
Service throws StudentNotFoundException
  ↓
GlobalExceptionHandler catches exception
  ↓
Returns 404 JSON error response
  ↓
Postman receives error
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1-6: Same as "Get Student by ID" flow**

(Request enters, reaches service, queries database...)

---

#### **Step 7: Database returns empty result**

```
H2 Database receives:
SELECT * FROM students WHERE id = 999;

Database searches... No row with id = 999 exists!

Returns: EMPTY RESULT
```

**What happens:** Database can't find student with ID 999, returns nothing.

---

#### **Step 8: Repository returns empty Optional**

```java
// JPA returns Optional.empty() when no result found
Optional<Student> result = studentRepository.findById(999L);
// result.isEmpty() == true
```

**What happens:** JPA wraps the empty result in `Optional.empty()`.

---

#### **Step 9: Service throws exception**

```java
// File: StudentService.java
public StudentResponse getStudentById(Long id) {
    Student student = studentRepository.findById(id)
            .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
            //         ↑
            // When Optional is empty, this lambda runs!
            // It creates and throws StudentNotFoundException
    
    return convertToResponse(student); // This line NEVER runs!
}
```

**What happens:**
- `Optional.empty()` triggers `.orElseThrow()`
- Lambda `() -> new StudentNotFoundException(...)` executes
- Exception is **thrown** (method stops immediately)
- Control jumps to exception handler

---

#### **Step 10: Custom exception class**

```java
// File: StudentNotFoundException.java
public class StudentNotFoundException extends RuntimeException {
    
    public StudentNotFoundException(String message) {
        super(message); // Pass message to parent RuntimeException class
    }
}
```

**What happens:** This is our custom exception class. It extends `RuntimeException` (unchecked exception).

---

#### **Step 11: GlobalExceptionHandler catches exception**

```java
// File: GlobalExceptionHandler.java

// Line 1: @RestControllerAdvice - Tells Spring:
// "This class handles exceptions from ALL @RestController classes"
// Global = catches exceptions from entire application
// Advantage: All error handling in ONE place (not scattered everywhere)
@RestControllerAdvice

// Line 2: Declare public class
public class GlobalExceptionHandler {

    // Line 3: @ExceptionHandler - Tells Spring:
    // "When StudentNotFoundException is thrown, call this method"
    // Spring searches all @RestControllerAdvice classes for matching handler
    @ExceptionHandler(StudentNotFoundException.class)
    
    // Line 4: @ResponseStatus - Sets HTTP status code
    // HttpStatus.NOT_FOUND = 404 Not Found
    // This automatically sets response status without ResponseEntity
    @ResponseStatus(HttpStatus.NOT_FOUND)
    
    // Line 5: Method declaration
    // - public = Spring can call it
    // - ErrorResponse = return type (custom error object)
    // - handleStudentNotFoundException = method name (descriptive!)
    // - StudentNotFoundException ex = the exception that was thrown
    //   Spring passes the actual exception object here
    public ErrorResponse handleStudentNotFoundException(StudentNotFoundException ex) {
        
        // Create error response object
        
        // Line 6: Create new empty ErrorResponse object
        ErrorResponse error = new ErrorResponse();
        
        // Line 7: Set timestamp to current date/time
        // LocalDateTime.now() gets current moment
        error.setTimestamp(LocalDateTime.now());
        
        // Line 8: Set HTTP status code (404)
        error.setStatus(404);
        
        // Line 9: Set error type ("Not Found")
        error.setError("Not Found");
        
        // Line 10: Set error message from exception
        // ex.getMessage() gets the message we set when creating exception
        // Example: "Student not found with id: 999"
        error.setMessage(ex.getMessage());
        
        // Line 11: Set request path (where error happened)
        // In real code, you'd get this from HttpServletRequest
        error.setPath("/api/students/999");
        
        // Line 12: Return error object
        // Spring automatically converts ErrorResponse → JSON
        // Combined with @ResponseStatus, this creates 404 response
        return error;
    }
}
```

**What happens:**
- `@RestControllerAdvice` tells Spring: "This class handles exceptions globally"
- `@ExceptionHandler(StudentNotFoundException.class)` catches our custom exception
- `@ResponseStatus(HttpStatus.NOT_FOUND)` sets HTTP status to 404
- Method creates `ErrorResponse` object with error details
- Spring converts ErrorResponse → JSON automatically

---

#### **Step 12: Error response sent to Postman**

```
HTTP/1.1 404 Not Found
Content-Type: application/json

{
    "timestamp": "2026-01-30T10:15:30",
    "status": 404,
    "error": "Not Found",
    "message": "Student not found with id: 999",
    "path": "/api/students/999"
}
```

**What happens:** Postman receives 404 error with JSON body explaining the problem.

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in Service:** `Student student = studentRepository.findById(id)`
   - Check what ID is being searched (999)

2. **Line in Service:** `.orElseThrow(() -> new StudentNotFoundException(...))`
   - See Optional is empty, exception is about to be thrown

3. **Inside lambda:** `new StudentNotFoundException("Student not found with id: " + id)`
   - See exception being created

4. **Line in GlobalExceptionHandler:** `public ErrorResponse handleStudentNotFoundException(...)`
   - Exception caught here! See exception object details

5. **Line in handler:** `error.setMessage(ex.getMessage());`
   - See error message being set

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **Exception** | Error object thrown when something goes wrong |
| **RuntimeException** | Type of exception that doesn't need try-catch (unchecked) |
| **@RestControllerAdvice** | Catches exceptions from all controllers globally |
| **@ExceptionHandler** | Specifies which exception type this method handles |
| **@ResponseStatus** | Sets HTTP status code for response (404, 500, etc.) |
| **Custom Exception** | Your own exception class (StudentNotFoundException) |
| **.orElseThrow()** | Throws exception if Optional is empty |

---

## 4) Health Check (GET)

### 1) What you send (Request)

- **Method:** GET
- **URL:** `http://localhost:8080/api/students/health`
- **Headers:** None
- **Body:** None

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example:**

```
REST API is running! ✓
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman 
  ↓
Tomcat → DispatcherServlet → StudentController.health()
  ↓
Returns simple String (no service/database calls)
  ↓
Postman receives text response
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: Request enters**

```
GET http://localhost:8080/api/students/health
```

---

#### **Step 2: Controller method runs**

```java
// File: StudentController.java
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @GetMapping("/health")
    public String health() {
        return "REST API is running! ✓";
    }
}
```

**What happens:**
- Very simple method!
- No service call, no database call
- Just returns a String
- Used to check if server is alive

---

#### **Step 3: Response sent**

```
HTTP/1.1 200 OK
Content-Type: text/plain

REST API is running! ✓
```

**What happens:** Spring sees return type is `String` (not an object), so it sends plain text instead of JSON.

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in Controller:** `return "REST API is running! ✓";`
   - See method executing

**Note:** This endpoint is too simple for detailed debugging. It's just for checking if server is running.

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **Health check endpoint** | Simple endpoint to verify server is running |
| **String return type** | Returns plain text instead of JSON |

---

# SOAP APIs

---

## 5) Create Student (SOAP)

### 1) What you send (Request)

- **Method:** POST
- **URL:** `http://localhost:8080/ws`
- **Headers:** 
  - `Content-Type: text/xml`
  - `SOAPAction: ""`
- **Body:**

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:soap="http://learning.com/crud/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:createStudentRequest>
         <soap:name>Jane Smith</soap:name>
         <soap:age>16</soap:age>
         <soap:grade>B</soap:grade>
      </soap:createStudentRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example:**

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <ns2:createStudentResponse xmlns:ns2="http://learning.com/crud/soap">
         <ns2:student>
            <ns2:id>2</ns2:id>
            <ns2:name>Jane Smith</ns2:name>
            <ns2:age>16</ns2:age>
            <ns2:grade>B</ns2:grade>
         </ns2:student>
      </ns2:createStudentResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman sends SOAP XML
  ↓
Tomcat → MessageDispatcherServlet (Spring WS)
  ↓
JAXB converts XML → Java objects (CreateStudentRequest)
  ↓
StudentSoapEndpoint.createStudent() [SOAP Layer]
  ↓
StudentService.createStudent() [Business Logic - SAME as REST!]
  ↓
StudentRepository.save() [Data Access]
  ↓
H2 Database
  ↓
Response travels back
  ↓
JAXB converts Java objects → XML (CreateStudentResponse)
  ↓
Postman receives SOAP XML
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: SOAP request enters Tomcat**

```xml
POST /ws
Content-Type: text/xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:soap="http://learning.com/crud/soap">
   <soapenv:Body>
      <soap:createStudentRequest>
         <soap:name>Jane Smith</soap:name>
         <soap:age>16</soap:age>
         <soap:grade>B</soap:grade>
      </soap:createStudentRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**What happens:** Postman sends XML (not JSON!) to `/ws` endpoint.

---

#### **Step 2: SoapConfig routes SOAP requests**

```java
// File: SoapConfig.java
@Configuration
@EnableWs  // Enables SOAP web services
public class SoapConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
        //                                                ↑
        //                           All URLs starting with /ws go to SOAP handler
    }
}
```

**What happens:** 
- `@EnableWs` activates SOAP support
- `MessageDispatcherServlet` handles SOAP requests (like DispatcherServlet for REST)
- All requests to `/ws/*` are treated as SOAP

---

#### **Step 3: JAXB converts XML → Java object**

```java
// Spring WS uses JAXB (Java Architecture for XML Binding) automatically
// It reads XSD schema (students.xsd) and generates Java classes

// Generated class: CreateStudentRequest.java
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"name", "age", "grade"})
@XmlRootElement(name = "createStudentRequest")
public class CreateStudentRequest {
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected int age;
    @XmlElement(required = true)
    protected String grade;
    
    // Getters and setters...
}
```

**What happens:**
- JAXB reads incoming XML
- Converts XML tags → Java object fields
  - `<soap:name>Jane Smith</soap:name>` → `request.name = "Jane Smith"`
  - `<soap:age>16</soap:age>` → `request.age = 16`
  - `<soap:grade>B</soap:grade>` → `request.grade = "B"`

---

#### **Step 4: StudentSoapEndpoint method runs**

```java
// File: StudentSoapEndpoint.java
@Endpoint  // Marks this as SOAP endpoint (like @RestController for REST)
public class StudentSoapEndpoint {

    private static final String NAMESPACE_URI = "http://learning.com/crud/soap";
    private final StudentService studentService;
    
    public StudentSoapEndpoint(StudentService studentService) {
        this.studentService = studentService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createStudentRequest")
    //           ↑                           ↑
    //    XML namespace in SOAP            Root element name in XML
    @ResponsePayload
    public CreateStudentResponse createStudent(@RequestPayload CreateStudentRequest request) {
        // Step 4a: Convert SOAP request → DTO (for service layer)
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName(request.getName());     // Jane Smith
        studentRequest.setAge(request.getAge());       // 16
        studentRequest.setGrade(request.getGrade());   // B
        
        // Step 4b: Call service (SAME service used by REST!)
        StudentResponse studentResponse = studentService.createStudent(studentRequest);
        
        // Step 4c: Convert DTO → SOAP response
        CreateStudentResponse response = new CreateStudentResponse();
        com.learning.soap.Student soapStudent = new com.learning.soap.Student();
        soapStudent.setId(studentResponse.getId());
        soapStudent.setName(studentResponse.getName());
        soapStudent.setAge(studentResponse.getAge());
        soapStudent.setGrade(studentResponse.getGrade());
        response.setStudent(soapStudent);
        
        return response;  // JAXB converts this to XML automatically
    }
}
```

**What happens:**
- `@Endpoint` marks class as SOAP endpoint
- `@PayloadRoot` matches XML namespace + element name to this method
- Method receives `CreateStudentRequest` (JAXB generated class)
- Converts SOAP objects → DTO → calls service → converts back to SOAP objects
- Returns `CreateStudentResponse`

---

#### **Step 5-8: Same as REST flow**

Service → Repository → Database → Save → Return (same code as REST API!)

**Key point:** SOAP and REST **share the same service layer**! Only the endpoint layer is different.

---

#### **Step 9: JAXB converts Java object → XML**

```java
// Spring WS automatically converts CreateStudentResponse to XML
CreateStudentResponse response = new CreateStudentResponse();
response.setStudent(student);

// JAXB converts object to XML:
<ns2:createStudentResponse xmlns:ns2="http://learning.com/crud/soap">
   <ns2:student>
      <ns2:id>2</ns2:id>
      <ns2:name>Jane Smith</ns2:name>
      <ns2:age>16</ns2:age>
      <ns2:grade>B</ns2:grade>
   </ns2:student>
</ns2:createStudentResponse>
```

**What happens:** JAXB reads Java object and converts fields → XML tags.

---

#### **Step 10: SOAP envelope wraps response**

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <!-- Your response goes inside Body -->
      <ns2:createStudentResponse xmlns:ns2="http://learning.com/crud/soap">
         <ns2:student>
            <ns2:id>2</ns2:id>
            <ns2:name>Jane Smith</ns2:name>
            <ns2:age>16</ns2:age>
            <ns2:grade>B</ns2:grade>
         </ns2:student>
      </ns2:createStudentResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

**What happens:** Spring WS wraps response in SOAP Envelope (standard SOAP format).

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in SoapEndpoint:** `StudentRequest studentRequest = new StudentRequest();`
   - See SOAP request object after XML → Java conversion

2. **Line in SoapEndpoint:** `StudentResponse studentResponse = studentService.createStudent(studentRequest);`
   - Verify DTO is correct before calling service

3. **Line in Service:** Same as REST breakpoints

4. **Line in SoapEndpoint:** `response.setStudent(soapStudent);`
   - See SOAP response object before Java → XML conversion

5. **Line in SoapEndpoint:** `return response;`
   - Final response object (will be converted to XML)

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **SOAP** | Old-style web service using XML messages (not JSON) |
| **XML** | Text format with tags like `<name>Value</name>` |
| **JAXB** | Java library that converts XML ↔ Java objects automatically |
| **XSD (XML Schema)** | File defining structure of XML (like a blueprint) |
| **@Endpoint** | SOAP version of @RestController |
| **@PayloadRoot** | Matches XML namespace + element to Java method |
| **@RequestPayload** | SOAP version of @RequestBody (XML → Java) |
| **@ResponsePayload** | SOAP version of @ResponseBody (Java → XML) |
| **Namespace** | XML namespace like `http://learning.com/crud/soap` (prevents name conflicts) |

---

## 6) Get Student by ID (SOAP)

### 1) What you send (Request)

- **Method:** POST
- **URL:** `http://localhost:8080/ws`
- **Headers:** 
  - `Content-Type: text/xml`
  - `SOAPAction: ""`
- **Body:**

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:soap="http://learning.com/crud/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:getStudentByIdRequest>
         <soap:id>1</soap:id>
      </soap:getStudentByIdRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example:**

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <ns2:getStudentByIdResponse xmlns:ns2="http://learning.com/crud/soap">
         <ns2:student>
            <ns2:id>1</ns2:id>
            <ns2:name>John Doe</ns2:name>
            <ns2:age>15</ns2:age>
            <ns2:grade>A</ns2:grade>
         </ns2:student>
      </ns2:getStudentByIdResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman sends SOAP XML with ID
  ↓
MessageDispatcherServlet
  ↓
JAXB converts XML → GetStudentByIdRequest object
  ↓
StudentSoapEndpoint.getStudentById()
  ↓
StudentService.getStudentById() [SAME as REST!]
  ↓
Database query
  ↓
Response back
  ↓
JAXB converts response → XML
  ↓
Postman receives SOAP XML
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1-3: SOAP request processing (same as create)**

---

#### **Step 4: StudentSoapEndpoint method runs**

```java
// File: StudentSoapEndpoint.java
@Endpoint
public class StudentSoapEndpoint {

    private static final String NAMESPACE_URI = "http://learning.com/crud/soap";
    private final StudentService studentService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentByIdRequest")
    @ResponsePayload
    public GetStudentByIdResponse getStudentById(@RequestPayload GetStudentByIdRequest request) {
        // Step 4a: Extract ID from SOAP request
        Long studentId = request.getId();  // 1L
        
        // Step 4b: Call service (SAME method as REST!)
        StudentResponse studentResponse = studentService.getStudentById(studentId);
        
        // Step 4c: Convert DTO → SOAP response
        GetStudentByIdResponse response = new GetStudentByIdResponse();
        com.learning.soap.Student soapStudent = new com.learning.soap.Student();
        soapStudent.setId(studentResponse.getId());
        soapStudent.setName(studentResponse.getName());
        soapStudent.setAge(studentResponse.getAge());
        soapStudent.setGrade(studentResponse.getGrade());
        response.setStudent(soapStudent);
        
        return response;
    }
}
```

**What happens:**
- Extracts ID from XML: `<soap:id>1</soap:id>` → `request.getId() = 1L`
- Calls **same service method** as REST
- Converts DTO → SOAP object
- Returns response (JAXB converts to XML)

---

#### **Step 5-8: Same as REST "Get by ID" flow**

(Service → Repository → Database → Query → Return)

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in SoapEndpoint:** `Long studentId = request.getId();`
   - See ID extracted from XML

2. **Line in SoapEndpoint:** `StudentResponse studentResponse = studentService.getStudentById(studentId);`
   - Before calling service

3. **Line in Service:** Same as REST breakpoints

4. **Line in SoapEndpoint:** `return response;`
   - Final SOAP response object

---

### 6) Tiny "Java concept used here" box

Same as "Create Student (SOAP)" plus:

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **XML element** | Single piece of data: `<id>1</id>` |
| **Code reuse** | SOAP and REST use SAME service layer (smart design!) |

---

## 7) Get WSDL

### 1) What you send (Request)

- **Method:** GET
- **URL:** `http://localhost:8080/ws/students.wsdl`
- **Headers:** None
- **Body:** None

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example (partial):**

```xml
<wsdl:definitions xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                  targetNamespace="http://learning.com/crud/soap">
    
    <wsdl:types>
        <!-- Data types from students.xsd -->
        <xs:schema>
            <xs:element name="createStudentRequest">
                <xs:complexType>
                    <xs:sequence>
                        <xs:element name="name" type="xs:string"/>
                        <xs:element name="age" type="xs:int"/>
                        <xs:element name="grade" type="xs:string"/>
                    </xs:sequence>
                </xs:complexType>
            </xs:element>
            <!-- More types... -->
        </xs:schema>
    </wsdl:types>
    
    <wsdl:message name="createStudentRequest">
        <wsdl:part name="parameters" element="tns:createStudentRequest"/>
    </wsdl:message>
    
    <!-- More operations... -->
    
    <wsdl:service name="StudentsService">
        <wsdl:port name="StudentsPort" binding="tns:StudentsBinding">
            <soap:address location="http://localhost:8080/ws"/>
        </wsdl:port>
    </wsdl:service>
</wsdl:definitions>
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman requests WSDL
  ↓
SoapConfig generates WSDL from XSD
  ↓
Returns XML document describing SOAP service
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: SoapConfig generates WSDL**

```java
// File: SoapConfig.java
@Configuration
@EnableWs
public class SoapConfig {

    @Bean(name = "students")  // WSDL will be at: /ws/students.wsdl
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema studentsSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("StudentsPort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace("http://learning.com/crud/soap");
        definition.setSchema(studentsSchema);  // Uses students.xsd
        return definition;
    }

    @Bean
    public XsdSchema studentsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/students.xsd"));
        //                                                     ↑
        //                          Loads students.xsd from resources folder
    }
}
```

**What happens:**
- Spring WS automatically generates WSDL from XSD schema
- WSDL = Web Service Description Language (contract document)
- Describes:
  - Available operations (createStudent, getStudentById)
  - Input/output message formats
  - Data types
  - Service endpoint URL

---

#### **Step 2: XSD Schema defines data structures**

```xml
<!-- File: students.xsd -->
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="http://learning.com/crud/soap">

    <!-- Request to create student -->
    <xs:element name="createStudentRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="name" type="xs:string"/>
                <xs:element name="age" type="xs:int"/>
                <xs:element name="grade" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
    
    <!-- Response after creating student -->
    <xs:element name="createStudentResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="student" type="tns:student"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
    
    <!-- Student data type -->
    <xs:complexType name="student">
        <xs:sequence>
            <xs:element name="id" type="xs:long"/>
            <xs:element name="name" type="xs:string"/>
            <xs:element name="age" type="xs:int"/>
            <xs:element name="grade" type="xs:string"/>
        </xs:sequence>
    </xs:complexType>
    
    <!-- More elements for getStudentById... -->
</xs:schema>
```

**What happens:**
- XSD = XML Schema Definition (blueprint for XML structure)
- Defines all request/response formats
- JAXB uses this to generate Java classes
- WSDL generation uses this to describe service

---

### 5) "Put breakpoint here" (Debug learning)

**No breakpoints needed!** WSDL is auto-generated. Just view it in browser.

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **WSDL** | Document describing SOAP service (like API documentation for SOAP) |
| **XSD** | XML Schema - defines structure of XML messages |
| **@Bean** | Creates Spring-managed object |
| **Auto-generation** | Spring generates WSDL automatically from XSD |

---

# GraphQL APIs

---

## 8) Create Student (Mutation)

### 1) What you send (Request)

- **Method:** POST
- **URL:** `http://localhost:8080/graphql`
- **Headers:** 
  - `Content-Type: application/json`
- **Body:**

```json
{
    "query": "mutation { createStudent(name: \"Alice Johnson\", age: 17, grade: \"A\") { id name age grade } }"
}
```

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example:**

```json
{
    "data": {
        "createStudent": {
            "id": "3",
            "name": "Alice Johnson",
            "age": 17,
            "grade": "A"
        }
    }
}
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman sends GraphQL mutation (JSON)
  ↓
GraphQL Engine parses query
  ↓
Validates query against schema.graphqls
  ↓
StudentGraphQLController.createStudent()
  ↓
StudentService.createStudent() [SAME service!]
  ↓
Database insert
  ↓
GraphQL Engine builds JSON response (only requested fields!)
  ↓
Postman receives JSON
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: GraphQL request enters**

```
POST /graphql
Content-Type: application/json

{
    "query": "mutation { createStudent(name: \"Alice Johnson\", age: 17, grade: \"A\") { id name age grade } }"
}
```

**What happens:** Unlike REST (different URLs) or SOAP (XML), GraphQL uses:
- **Single endpoint:** `/graphql`
- **Query in request body:** Describes what you want to do

---

#### **Step 2: GraphQL schema defines operations**

```graphql
# File: schema.graphqls
type Student {
    id: ID!          # ! means required (not null)
    name: String!
    age: Int!
    grade: String!
}

type Query {
    studentById(id: ID!): Student
}

type Mutation {
    createStudent(name: String!, age: Int!, grade: String!): Student
    #             ↑ Input parameters                         ↑ Return type
}
```

**What happens:**
- GraphQL schema is like a contract (similar to WSDL for SOAP)
- Defines:
  - `Student` type with 4 fields
  - `Mutation` type with createStudent operation
  - Input parameters and return type
- `!` means field is required (cannot be null)

---

#### **Step 3: GraphQL engine parses and validates query**

```
GraphQL engine reads:
"mutation { createStudent(name: \"Alice Johnson\", age: 17, grade: \"A\") { id name age grade } }"

Parses:
- Operation type: mutation (not query)
- Operation name: createStudent
- Arguments: name="Alice Johnson", age=17, grade="A"
- Requested fields: id, name, age, grade

Validates against schema:
✓ createStudent exists in Mutation type
✓ Arguments match schema (name: String!, age: Int!, grade: String!)
✓ Return fields (id, name, age, grade) exist in Student type
```

**What happens:** GraphQL validates your query before executing it. Invalid queries are rejected immediately.

---

#### **Step 4: StudentGraphQLController method runs**

```java
// File: StudentGraphQLController.java
@Controller  // GraphQL uses @Controller (not @RestController)
public class StudentGraphQLController {

    private final StudentService studentService;
    
    public StudentGraphQLController(StudentService studentService) {
        this.studentService = studentService;
    }

    @MutationMapping  // Maps to GraphQL mutations (create, update, delete)
    public StudentResponse createStudent(
            @Argument String name,    // Extracts "name" argument from GraphQL query
            @Argument Integer age,    // Extracts "age" argument
            @Argument String grade) { // Extracts "grade" argument
        
        // Step 4a: Create DTO from arguments
        StudentRequest request = new StudentRequest();
        request.setName(name);      // "Alice Johnson"
        request.setAge(age);        // 17
        request.setGrade(grade);    // "A"
        
        // Step 4b: Call service (SAME method as REST and SOAP!)
        return studentService.createStudent(request);
    }
}
```

**What happens:**
- `@Controller` marks class for GraphQL (different from `@RestController`)
- `@MutationMapping` maps method to GraphQL mutation
- `@Argument` extracts arguments from GraphQL query
  - GraphQL: `createStudent(name: "Alice", age: 17, grade: "A")`
  - Java: Method parameters `name="Alice"`, `age=17`, `grade="A"`
- Calls **same service** as REST and SOAP (code reuse!)

---

#### **Step 5-8: Same as REST create flow**

(Service → Repository → Database → Insert → Return)

---

#### **Step 9: GraphQL engine builds response with ONLY requested fields**

```java
// Service returns complete StudentResponse:
{
    id: 3,
    name: "Alice Johnson",
    age: 17,
    grade: "A"
}

// GraphQL engine reads query:
"{ id name age grade }"  ← All fields requested
//  ↑  ↑    ↑   ↑
// Return all 4 fields

// Final JSON response:
{
    "data": {
        "createStudent": {
            "id": "3",
            "name": "Alice Johnson",
            "age": 17,
            "grade": "A"
        }
    }
}
```

**What happens:** GraphQL engine:
1. Receives StudentResponse from service (all fields)
2. Reads query to see which fields were requested
3. Builds JSON with ONLY requested fields
4. This is GraphQL's superpower: **no over-fetching!**

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in GraphQLController:** `StudentRequest request = new StudentRequest();`
   - See arguments extracted from GraphQL query

2. **Line in GraphQLController:** `request.setName(name);`
   - Verify values are correct

3. **Line in GraphQLController:** `return studentService.createStudent(request);`
   - Before calling service

4. **Line in Service:** Same as REST breakpoints

5. **After return in GraphQLController:** 
   - See StudentResponse returned to GraphQL engine

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **GraphQL** | Modern API style: single endpoint, request only needed fields |
| **Mutation** | GraphQL operation that changes data (create, update, delete) |
| **Query** | GraphQL operation that reads data (no changes) |
| **Schema** | Contract defining available operations and data types (.graphqls file) |
| **@Controller** | For GraphQL (different from @RestController) |
| **@MutationMapping** | Maps Java method to GraphQL mutation |
| **@Argument** | Extracts argument from GraphQL query |
| **Field selection** | Client specifies which fields to return (prevents over-fetching) |

---

## 9) Get Student by ID (Query)

### 1) What you send (Request)

- **Method:** POST
- **URL:** `http://localhost:8080/graphql`
- **Headers:** 
  - `Content-Type: application/json`
- **Body:**

```json
{
    "query": "query { studentById(id: 1) { id name age grade } }"
}
```

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example:**

```json
{
    "data": {
        "studentById": {
            "id": "1",
            "name": "John Doe",
            "age": 15,
            "grade": "A"
        }
    }
}
```

### 3) Code flow: "Who calls whom" (Short map)

```
Postman sends GraphQL query
  ↓
GraphQL Engine validates against schema
  ↓
StudentGraphQLController.studentById()
  ↓
StudentService.getStudentById() [SAME as REST/SOAP!]
  ↓
Database query
  ↓
GraphQL builds response with requested fields only
  ↓
Postman receives JSON
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1-3: GraphQL query processing (similar to mutation)**

---

#### **Step 4: StudentGraphQLController method runs**

```java
// File: StudentGraphQLController.java
@Controller
public class StudentGraphQLController {

    private final StudentService studentService;

    @QueryMapping  // Maps to GraphQL queries (read operations)
    public StudentResponse studentById(@Argument Long id) {
        //                              ↑
        // Extracts "id" argument from GraphQL query: studentById(id: 1)
        
        // Call service (SAME method as REST and SOAP!)
        return studentService.getStudentById(id);
    }
}
```

**What happens:**
- `@QueryMapping` maps method to GraphQL **query** (read operation)
- `@Argument Long id` extracts ID from query
- GraphQL query: `studentById(id: 1)` → Java: `id = 1L`
- Calls **same service method** as REST and SOAP

---

#### **Step 5-8: Same as REST "Get by ID" flow**

(Service → Repository → Database → SELECT → Return)

---

#### **Step 9: GraphQL returns requested fields**

```
Query requested: { id name age grade }  ← All 4 fields

GraphQL returns all 4 fields:
{
    "data": {
        "studentById": {
            "id": "1",
            "name": "John Doe",
            "age": 15,
            "grade": "A"
        }
    }
}
```

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in GraphQLController:** `return studentService.getStudentById(id);`
   - Check ID extracted from query

2. **Line in Service:** Same as REST breakpoints

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **@QueryMapping** | Maps Java method to GraphQL query (read operation) |
| **Query vs Mutation** | Query = read data, Mutation = change data |

---

## 10) Get Student - Only Name (Selective)

### 1) What you send (Request)

- **Method:** POST
- **URL:** `http://localhost:8080/graphql`
- **Headers:** 
  - `Content-Type: application/json`
- **Body:**

```json
{
    "query": "query { studentById(id: 1) { name } }"
}
```

**Notice:** Requesting ONLY `name` field (not id, age, grade)

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response example:**

```json
{
    "data": {
        "studentById": {
            "name": "John Doe"
        }
    }
}
```

**Notice:** Response contains ONLY `name` field!

### 3) Code flow: "Who calls whom" (Short map)

```
Postman sends query requesting ONLY name field
  ↓
GraphQL Engine validates
  ↓
StudentGraphQLController.studentById() [SAME method as before!]
  ↓
Service returns FULL StudentResponse (id, name, age, grade)
  ↓
GraphQL Engine filters response → returns ONLY name
  ↓
Postman receives JSON with name only
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: Query requests only ONE field**

```
Query: "query { studentById(id: 1) { name } }"
                                       ↑
                            Only "name" field requested!
```

---

#### **Step 2-7: SAME as previous "Get by ID" flow**

Controller → Service → Repository → Database → Query → Return

**Important:** Service returns **complete** StudentResponse with all 4 fields!

```java
// Service returns ALL fields:
StudentResponse {
    id: 1,
    name: "John Doe",
    age: 15,
    grade: "A"
}
```

---

#### **Step 8: GraphQL Engine performs field filtering**

```
GraphQL Engine receives complete StudentResponse:
{
    id: 1,
    name: "John Doe",
    age: 15,
    grade: "A"
}

Reads query to see requested fields:
"{ name }"  ← Only name requested!

Filters response to include ONLY requested field:
{
    "data": {
        "studentById": {
            "name": "John Doe"
            // id, age, grade NOT included!
        }
    }
}
```

**What happens:** 
- GraphQL engine automatically filters response
- Returns ONLY fields specified in query
- This is GraphQL's **biggest advantage**: no over-fetching!
- REST would return all fields always

---

### 5) "Put breakpoint here" (Debug learning)

1. **Line in GraphQLController:** `return studentService.getStudentById(id);`
   - See that service returns FULL StudentResponse (all fields)

2. **After return statement:**
   - GraphQL engine filters response (this happens automatically, no code to debug)

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **Field selection** | Client decides which fields to return (GraphQL filters automatically) |
| **Over-fetching** | REST returns all fields always (wasteful); GraphQL returns only requested fields (efficient) |
| **Under-fetching** | REST may require multiple requests; GraphQL gets exactly what you need in one request |

---

# Utilities

---

## 11) H2 Console (Browser)

### 1) What you send (Request)

- **Method:** GET
- **URL:** `http://localhost:8080/h2-console`
- **Headers:** None
- **Body:** None

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response:** HTML page (not JSON/XML!)

**What you see in browser:**

```
H2 Console Login Page
---------------------
Saved Settings: Generic H2 (Embedded)
Setting Name:   Generic H2 (Embedded)

Driver Class:   org.h2.Driver
JDBC URL:       jdbc:h2:mem:testdb    ← Use this
User Name:      sa                     ← Use this
Password:       [leave empty]          ← Leave empty

[Test Connection] [Connect]
```

### 3) Code flow: "Who calls whom" (Short map)

```
Browser opens /h2-console
  ↓
Spring Boot's H2 Console (built-in web app)
  ↓
Shows login page (HTML)
  ↓
After login → Interactive SQL interface
  ↓
You can run SQL queries directly!
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: H2 Console enabled in configuration**

```properties
# File: application.properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**What happens:** Spring Boot includes H2 Console (a mini web application for database management).

---

#### **Step 2: Access H2 Console in browser**

```
Open browser: http://localhost:8080/h2-console

You see a login form
```

---

#### **Step 3: Connect to database**

```
Fill in:
JDBC URL: jdbc:h2:mem:testdb    (from application.properties)
Username: sa                     (from application.properties)
Password: (leave empty)

Click "Connect"
```

**What happens:** H2 Console connects to your in-memory database.

---

#### **Step 4: Run SQL queries**

```sql
-- View all students
SELECT * FROM students;

-- Result:
| ID | NAME          | AGE | GRADE |
|----|---------------|-----|-------|
| 1  | John Doe      | 15  | A     |
| 2  | Jane Smith    | 16  | B     |
| 3  | Alice Johnson | 17  | A     |

-- Insert student directly (bypassing APIs!)
INSERT INTO students (name, age, grade) VALUES ('Bob Wilson', 14, 'C');

-- Update student
UPDATE students SET grade = 'A+' WHERE id = 1;

-- Delete student
DELETE FROM students WHERE id = 2;
```

**What happens:** H2 Console lets you:
- View database tables
- Run SQL queries manually
- Insert/update/delete data directly
- See database structure
- **Perfect for learning and debugging!**

---

### 5) "Put breakpoint here" (Debug learning)

**No breakpoints!** This is a browser tool, not API code.

**Use it to:**
- Verify data was saved after API calls
- See actual SQL table structure
- Debug database issues
- Learn SQL queries

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **H2 Console** | Built-in web tool to view/query database |
| **In-memory database** | Database stored in RAM (disappears when app stops) |
| **JDBC URL** | Connection string to database (like a web address for database) |
| **SQL** | Language to query databases (SELECT, INSERT, UPDATE, DELETE) |

---

## 12) GraphiQL Playground (Browser)

### 1) What you send (Request)

- **Method:** GET
- **URL:** `http://localhost:8080/graphiql`
- **Headers:** None
- **Body:** None

### 2) What you get back (Expected Response)

- **Status:** 200 OK
- **Response:** Interactive GraphQL IDE (HTML page)

**What you see in browser:**

```
┌─────────────────────────────────────────────────────────┐
│ GraphiQL - GraphQL Playground                          │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  # Write your query here:                              │
│  query {                                               │
│    studentById(id: 1) {   ← Auto-complete!           │
│      id                                                │
│      name                                              │
│      age                                               │
│      grade                                             │
│    }                                                   │
│  }                                                     │
│                                                         │
│  [▶ Execute Query]                                     │
│                                                         │
├─────────────────────────────────────────────────────────┤
│  Response:                                             │
│  {                                                     │
│    "data": {                                           │
│      "studentById": {                                  │
│        "id": "1",                                      │
│        "name": "John Doe",                            │
│        "age": 15,                                      │
│        "grade": "A"                                    │
│      }                                                 │
│    }                                                   │
│  }                                                     │
└─────────────────────────────────────────────────────────┘

Docs (right panel):
- Query
  - studentById(id: ID!): Student
- Mutation
  - createStudent(name: String!, age: Int!, grade: String!): Student
- Type: Student
  - id: ID!
  - name: String!
  - age: Int!
  - grade: String!
```

### 3) Code flow: "Who calls whom" (Short map)

```
Browser opens /graphiql
  ↓
Spring GraphQL's GraphiQL (built-in IDE)
  ↓
Shows interactive editor (HTML + JavaScript)
  ↓
You write GraphQL queries
  ↓
Click "Execute" → Sends to /graphql endpoint
  ↓
Results shown in right panel
```

### 4) Step-by-step code execution (WITH CODE BLOCKS)

#### **Step 1: GraphiQL enabled in configuration**

```properties
# File: application.properties
spring.graphql.graphiql.enabled=true
spring.graphql.graphiql.path=/graphiql
```

**What happens:** Spring Boot includes GraphiQL (interactive GraphQL IDE).

---

#### **Step 2: Access GraphiQL in browser**

```
Open browser: http://localhost:8080/graphiql

You see:
- Left panel: Query editor (write your queries here)
- Right panel: Results (after clicking Execute)
- Top right: Documentation (click "Docs" to see schema)
```

---

#### **Step 3: Try a query**

```graphql
# Type in left panel:
query {
  studentById(id: 1) {
    id
    name
    age
    grade
  }
}

# Click "Execute Query" button (▶)

# Right panel shows result:
{
  "data": {
    "studentById": {
      "id": "1",
      "name": "John Doe",
      "age": 15,
      "grade": "A"
    }
  }
}
```

---

#### **Step 4: Try a mutation**

```graphql
# Type in left panel:
mutation {
  createStudent(name: "Charlie Brown", age: 16, grade: "B") {
    id
    name
    age
    grade
  }
}

# Click Execute

# Result:
{
  "data": {
    "createStudent": {
      "id": "4",
      "name": "Charlie Brown",
      "age": 16,
      "grade": "B"
    }
  }
}
```

---

#### **Step 5: Explore documentation**

```
Click "Docs" button (top right)

Shows:
- All available queries
- All available mutations
- All types (Student, etc.)
- Required/optional fields
- Field types

Example:
Query.studentById
  Arguments:
    id: ID! (required)
  Returns:
    Student (may be null)
```

---

#### **Step 6: Use auto-complete**

```
Start typing:
query {
  stu...  ← Press Ctrl+Space

Auto-complete suggests:
- studentById

Continue:
studentById(id: 1) {
  n...  ← Press Ctrl+Space

Auto-complete suggests:
- name
- Not: (random field) → only valid fields shown!
```

---

### 5) "Put breakpoint here" (Debug learning)

**No breakpoints!** This is a browser tool.

**Use it to:**
- Learn GraphQL syntax interactively
- Test queries without Postman
- Explore schema documentation
- See real-time validation (invalid queries show errors immediately)

---

### 6) Tiny "Java concept used here" box

| **Concept** | **Simple Explanation** |
|-------------|------------------------|
| **GraphiQL** | Interactive web IDE for GraphQL (like Postman but built-in) |
| **IDE (Integrated Development Environment)** | Tool to write/test code (here: GraphQL queries) |
| **Auto-complete** | Suggests valid fields as you type |
| **Schema introspection** | GraphQL can describe itself (what queries/mutations are available) |
| **Real-time validation** | Invalid queries show errors before sending |

---

# Summary: REST vs SOAP vs GraphQL

| **Aspect** | **REST** | **SOAP** | **GraphQL** |
|------------|----------|----------|-------------|
| **Format** | JSON | XML | JSON |
| **Endpoints** | Multiple URLs | Single endpoint `/ws` | Single endpoint `/graphql` |
| **Protocol** | HTTP methods (GET, POST, PUT, DELETE) | XML messages over HTTP | POST with query in body |
| **Over-fetching** | Returns all fields always | Returns all fields always | Returns only requested fields |
| **Schema** | No standard schema | WSDL document | GraphQL schema (.graphqls) |
| **Learning curve** | Easy | Hard (XML, WSDL, namespaces) | Medium (new query language) |
| **Best for** | Simple CRUD, mobile apps | Enterprise systems, security | Complex queries, mobile apps, flexible clients |

---

# Common Java Concepts Across All APIs

| **Concept** | **Explanation** |
|-------------|-----------------|
| **Layer separation** | Controller → Service → Repository → Database (clean architecture) |
| **DTO (Data Transfer Object)** | Objects for transferring data between layers (StudentRequest, StudentResponse) |
| **Entity** | Objects representing database tables (Student with @Entity) |
| **Dependency Injection** | Spring provides objects automatically via constructor |
| **JPA/Hibernate** | Converts Java objects ↔ SQL queries automatically |
| **Repository pattern** | Interface for database operations (Spring implements it) |
| **Exception handling** | @RestControllerAdvice catches exceptions globally |
| **Code reuse** | REST, SOAP, GraphQL all use SAME service layer! |

---

# Debugging Tips for Beginners

1. **Start with breakpoints in Controller**
   - See incoming requests first
   - Verify parameters are extracted correctly

2. **Move to Service layer**
   - Check business logic
   - See data before/after database operations

3. **Check Repository layer**
   - Verify database queries

4. **Use H2 Console**
   - Manually check if data was saved correctly
   - Run SQL queries to verify

5. **Use GraphiQL for GraphQL**
   - Test queries interactively
   - See auto-complete and validation

6. **Read stack traces**
   - Exceptions show where error happened
   - Read from top to bottom (most recent first)

7. **Enable SQL logging**
   - `spring.jpa.show-sql=true` in application.properties
   - See actual SQL queries in console

---

# Learning Path Recommendation

1. **Week 1:** Master REST APIs (easiest)
   - Create/Read operations
   - Understand Controller → Service → Repository flow
   - Practice with Postman

2. **Week 2:** Learn exception handling
   - Try "Not Found" scenarios
   - Understand @RestControllerAdvice
   - Debug with breakpoints

3. **Week 3:** Explore H2 Console
   - View database tables
   - Run SQL queries manually
   - Understand JPA's SQL generation

4. **Week 4:** Try GraphQL
   - Use GraphiQL playground
   - Experiment with field selection
   - Compare with REST

5. **Week 5:** Understand SOAP (hardest)
   - View WSDL in browser
   - Understand XML structure
   - Compare with REST/GraphQL

---

# End of Guide

🎉 **Congratulations!** You now understand:
- How each API call flows through the code
- What happens at each layer
- How to debug with breakpoints
- Differences between REST, SOAP, and GraphQL

**Next steps:**
1. Run the application: `mvn spring-boot:run`
2. Import Postman collection
3. Try each API call while reading this guide
4. Put breakpoints and step through code
5. Experiment with H2 Console and GraphiQL

**Happy Learning!** 🚀
