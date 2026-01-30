# 📚 Java 17 CRUD Application - Complete Beginner's Guide

Welcome! This is a complete, working Java application that demonstrates **CRUD operations** (Create, Read, Update, Delete) using **REST, SOAP, and GraphQL APIs**.

This guide is written for complete beginners. Everything is explained in simple words, like you're learning Java for the first time.

---

## 🎯 What This Project Does

This application manages **Students** with these fields:
- **ID**: Unique number (auto-generated)
- **Name**: Student's name
- **Age**: Student's age
- **Grade**: Student's grade (A, B, C, etc.)

You can:
- ✅ Create a new student
- ✅ Get a student by ID
- ✅ Access the data through 3 different ways: REST, SOAP, and GraphQL

---

## 🗂️ Project Structure (Folder Tree)

```
Java-17-CRUD/
│
├── pom.xml                          # Maven configuration (dependencies)
├── README.md                        # This file!
│
└── src/
    └── main/
        ├── java/com/learning/crud/
        │   │
        │   ├── CrudApplication.java           # 🚀 ENTRY POINT - Application starts here
        │   │
        │   ├── entity/
        │   │   └── Student.java               # 📦 Database table blueprint
        │   │
        │   ├── dto/
        │   │   ├── StudentRequest.java        # 📥 Data coming FROM client
        │   │   └── StudentResponse.java       # 📤 Data going TO client
        │   │
        │   ├── repository/
        │   │   └── StudentRepository.java     # 🗄️ Talks to database
        │   │
        │   ├── service/
        │   │   └── StudentService.java        # 🧠 Business logic
        │   │
        │   ├── controller/
        │   │   └── StudentController.java     # 🌐 REST API endpoints
        │   │
        │   ├── graphql/
        │   │   └── StudentGraphQLController.java  # ⚡ GraphQL endpoints
        │   │
        │   ├── soap/
        │   │   └── StudentSoapEndpoint.java   # 📨 SOAP endpoints
        │   │
        │   ├── config/
        │   │   └── SoapConfig.java            # ⚙️ SOAP configuration
        │   │
        │   └── exception/
        │       ├── StudentNotFoundException.java    # ❌ Custom error
        │       └── GlobalExceptionHandler.java      # 🛡️ Error handler
        │
        └── resources/
            ├── application.properties         # ⚙️ App settings
            ├── graphql/
            │   └── schema.graphqls            # 📋 GraphQL schema
            └── xsd/
                └── students.xsd               # 📋 SOAP schema
```

---

## 🚀 How to Run This Application

### Prerequisites
- **Java 17** installed ([Download here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **Maven** installed ([Download here](https://maven.apache.org/download.cgi))
- Any text editor or IDE (VS Code, IntelliJ IDEA, Eclipse)

### Step-by-Step Instructions

1. **Open Terminal/Command Prompt**

2. **Navigate to project folder**
   ```bash
   cd "e:\Advanced Projects\Java-17-CRUD"
   ```

3. **Generate SOAP classes from XSD** (first time only)
   ```bash
   mvn clean compile
   ```
   This creates Java classes from `students.xsd` file.

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Wait for startup message**
   ```
   🚀 Application Started Successfully!
   📍 REST API: http://localhost:8080/api/students
   📍 SOAP WSDL: http://localhost:8080/ws/students.wsdl
   📍 GraphQL: http://localhost:8080/graphql
   📍 H2 Console: http://localhost:8080/h2-console
   ```

6. **Application is running!** 🎉

---

## 🧪 Testing the APIs

### 📮 Option 1: Use Postman Collection (Recommended!)

**Import the ready-made Postman collection:**

1. Download/Install Postman: https://www.postman.com/downloads/
2. Open Postman
3. Click **Import** → **File**
4. Select: `Student-CRUD-API.postman_collection.json`
5. Click **Import**
6. Start testing!

**Collection includes:**
- ✅ All 6 API endpoints (REST, SOAP, GraphQL)
- ✅ Automatic tests and validations
- ✅ Pre-configured headers and variables
- ✅ Detailed documentation for each request
- ✅ Browser links (H2 Console, GraphiQL)

📖 **Full Guide:** See [POSTMAN_GUIDE.md](POSTMAN_GUIDE.md) for detailed instructions!

---

### 📋 Option 2: Manual Testing (Command Line)

### 1️⃣ REST API Examples

#### **Create a Student (POST)**

**Using curl:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"John Doe\",\"age\":15,\"grade\":\"A\"}"
```

**Using PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/students" -Method Post -ContentType "application/json" -Body '{"name":"John Doe","age":15,"grade":"A"}'
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "age": 15,
  "grade": "A"
}
```

---

#### **Get Student by ID (GET)**

**Using curl:**
```bash
curl http://localhost:8080/api/students/1
```

**Using PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/students/1"
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "age": 15,
  "grade": "A"
}
```

---

### 2️⃣ SOAP API Examples

**SOAP Endpoint:** `http://localhost:8080/ws`  
**WSDL:** `http://localhost:8080/ws/students.wsdl`

#### **Create a Student (SOAP Request)**

**Request XML:**
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

**Using curl:**
```bash
curl -X POST http://localhost:8080/ws \
  -H "Content-Type: text/xml" \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://learning.com/crud/soap"><soapenv:Header/><soapenv:Body><soap:createStudentRequest><soap:name>Jane Smith</soap:name><soap:age>16</soap:age><soap:grade>B</soap:grade></soap:createStudentRequest></soapenv:Body></soapenv:Envelope>'
```

**Response XML:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <ns2:createStudentResponse xmlns:ns2="http://learning.com/crud/soap">
         <ns2:student>
            <ns2:id>1</ns2:id>
            <ns2:name>Jane Smith</ns2:name>
            <ns2:age>16</ns2:age>
            <ns2:grade>B</ns2:grade>
         </ns2:student>
      </ns2:createStudentResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

---

#### **Get Student by ID (SOAP Request)**

**Request XML:**
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

---

### 3️⃣ GraphQL API Examples

**GraphQL Endpoint:** `http://localhost:8080/graphql`  
**GraphiQL UI:** `http://localhost:8080/graphiql` (Interactive playground!)

#### **Create a Student (Mutation)**

**GraphQL Mutation:**
```graphql
mutation {
  createStudent(name: "Alice Johnson", age: 17, grade: "A") {
    id
    name
    age
    grade
  }
}
```

**Using curl:**
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"mutation { createStudent(name: \"Alice Johnson\", age: 17, grade: \"A\") { id name age grade } }"}'
```

**Response:**
```json
{
  "data": {
    "createStudent": {
      "id": "1",
      "name": "Alice Johnson",
      "age": 17,
      "grade": "A"
    }
  }
}
```

---

#### **Get Student by ID (Query)**

**GraphQL Query:**
```graphql
query {
  studentById(id: 1) {
    id
    name
    age
    grade
  }
}
```

**Using curl:**
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"query { studentById(id: 1) { id name age grade } }"}'
```

**Response:**
```json
{
  "data": {
    "studentById": {
      "id": "1",
      "name": "Alice Johnson",
      "age": 17,
      "grade": "A"
    }
  }
}
```

---

### 4️⃣ H2 Database Console

**URL:** `http://localhost:8080/h2-console`

**Connection Settings:**
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave empty)

Click "Connect" to see the database tables and run SQL queries!

---

## 🔍 Flow Explanation: How Each Endpoint Works

### 🌐 REST API Flow: POST /api/students

**What happens when you call: `POST http://localhost:8080/api/students`**

```
┌─────────────────────────────────────────────────────────────────┐
│ Step 1: HTTP Request Arrives                                    │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ Client sends JSON:                                              │
│ {                                                               │
│   "name": "John Doe",                                           │
│   "age": 15,                                                    │
│   "grade": "A"                                                  │
│ }                                                               │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 2: Spring Receives Request                                 │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ Spring Boot's embedded Tomcat server receives HTTP request      │
│ Spring looks for a controller that handles POST /api/students   │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 3: Controller Method is Called                             │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ File: StudentController.java                                    │
│ Method: createStudent(@RequestBody StudentRequest request)      │
│                                                                 │
│ Spring automatically converts JSON → StudentRequest object      │
│ This is called "deserialization"                                │
│                                                                 │
│ StudentRequest object now contains:                             │
│   - name = "John Doe"                                           │
│   - age = 15                                                    │
│   - grade = "A"                                                 │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 4: Service Method is Called                                │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ File: StudentService.java                                       │
│ Method: createStudent(StudentRequest request)                   │
│                                                                 │
│ Service creates a new Student entity:                           │
│   Student student = new Student();                              │
│   student.setName("John Doe");                                  │
│   student.setAge(15);                                           │
│   student.setGrade("A");                                        │
│                                                                 │
│ Why convert DTO → Entity?                                       │
│ - DTO = data from outside world                                 │
│ - Entity = data for database                                    │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 5: Repository Method is Called                             │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ File: StudentRepository.java                                    │
│ Method: save(student)                                           │
│                                                                 │
│ Repository is an interface (no code!)                           │
│ Spring JPA provides the implementation automatically            │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 6: JPA/Hibernate Generates SQL                             │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ Hibernate (JPA implementation) generates SQL:                   │
│                                                                 │
│ INSERT INTO students (name, age, grade)                         │
│ VALUES ('John Doe', 15, 'A');                                   │
│                                                                 │
│ You don't write this SQL - Hibernate does it automatically!     │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 7: Database Saves the Data                                 │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ H2 in-memory database executes the SQL                          │
│ Database generates an ID (e.g., 1)                              │
│ Data is stored in memory                                        │
│                                                                 │
│ Database returns the saved student with ID:                     │
│   id = 1                                                        │
│   name = "John Doe"                                             │
│   age = 15                                                      │
│   grade = "A"                                                   │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 8: Service Converts Entity → DTO                           │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ Service creates StudentResponse:                                │
│   response.setId(1);                                            │
│   response.setName("John Doe");                                 │
│   response.setAge(15);                                          │
│   response.setGrade("A");                                       │
│                                                                 │
│ Returns response to controller                                  │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 9: Controller Returns Response                             │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ Controller receives StudentResponse                             │
│ Wraps it in ResponseEntity with HTTP status 201 (Created)       │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 10: Spring Converts Response → JSON                        │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ Spring converts StudentResponse object → JSON                   │
│ This is called "serialization"                                  │
│                                                                 │
│ JSON Response:                                                  │
│ {                                                               │
│   "id": 1,                                                      │
│   "name": "John Doe",                                           │
│   "age": 15,                                                    │
│   "grade": "A"                                                  │
│ }                                                               │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ Step 11: HTTP Response Sent to Client                           │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│ HTTP Status: 201 Created                                        │
│ Content-Type: application/json                                  │
│ Body: {"id":1,"name":"John Doe","age":15,"grade":"A"}          │
└─────────────────────────────────────────────────────────────────┘
```

---

### 🌐 REST API Flow: GET /api/students/{id}

**What happens when you call: `GET http://localhost:8080/api/students/1`**

```
Step 1: HTTP GET request arrives
        ↓
Step 2: Spring routes to StudentController.getStudentById(id=1)
        ↓
Step 3: Controller calls StudentService.getStudentById(1)
        ↓
Step 4: Service calls StudentRepository.findById(1)
        ↓
Step 5: JPA generates SQL: SELECT * FROM students WHERE id = 1
        ↓
Step 6: H2 database executes query and returns student data
        ↓
Step 7: If found: Continue to step 8
        If not found: Throw StudentNotFoundException → Go to Exception Handler
        ↓
Step 8: Service converts Entity → StudentResponse DTO
        ↓
Step 9: Controller wraps response in ResponseEntity (HTTP 200)
        ↓
Step 10: Spring converts StudentResponse → JSON
        ↓
Step 11: HTTP response sent to client
```

---

### 📨 SOAP API Flow: createStudentRequest

**What happens when you call SOAP endpoint with createStudentRequest**

```
Step 1: SOAP XML request arrives at http://localhost:8080/ws
        ↓
Step 2: MessageDispatcherServlet receives the request
        ↓
Step 3: Spring WS parses XML and identifies operation: createStudentRequest
        ↓
Step 4: Spring WS converts XML → CreateStudentRequest Java object (JAXB)
        ↓
Step 5: Calls StudentSoapEndpoint.createStudent() method
        ↓
Step 6: Endpoint extracts data from SOAP request object
        ↓
Step 7: Creates StudentRequest DTO
        ↓
Step 8: Calls StudentService.createStudent() (SAME service as REST!)
        ↓
Step 9: Service → Repository → Database (same flow as REST)
        ↓
Step 10: Service returns StudentResponse
        ↓
Step 11: Endpoint converts StudentResponse → CreateStudentResponse (SOAP object)
        ↓
Step 12: Spring WS converts CreateStudentResponse → XML (JAXB)
        ↓
Step 13: SOAP XML response sent to client
```

**Key Point:** SOAP and REST use the SAME service layer! Only the entry point (controller/endpoint) is different.

---

### ⚡ GraphQL API Flow: createStudent Mutation

**What happens when you call GraphQL mutation**

```
Step 1: POST request with GraphQL query/mutation arrives at /graphql
        ↓
Step 2: Spring GraphQL parses the request
        ↓
Step 3: Identifies operation type: Mutation - createStudent
        ↓
Step 4: Extracts parameters: name, age, grade
        ↓
Step 5: Calls StudentGraphQLController.createStudent() method
        ↓
Step 6: Controller creates StudentRequest DTO
        ↓
Step 7: Calls StudentService.createStudent() (SAME service!)
        ↓
Step 8: Service → Repository → Database (same flow)
        ↓
Step 9: Service returns StudentResponse
        ↓
Step 10: GraphQL controller returns StudentResponse
        ↓
Step 11: Spring GraphQL converts response → JSON
        ↓
Step 12: Returns ONLY the fields requested by client
        ↓
Step 13: JSON response sent to client
```

**Key Difference:** GraphQL client can choose which fields to receive (id, name, age, grade) - REST returns all fields.

---

## 📖 Java Concepts Explained (Like You're 12 Years Old)

### 1. **Class**
Think of a class like a **blueprint** for building something.

Example: `Student` class is a blueprint for creating student objects.

```java
public class Student {
    private String name;
    private int age;
}
```

### 2. **Object**
An object is an **actual thing** created from a class blueprint.

```java
Student john = new Student();  // "john" is an object
john.setName("John Doe");
john.setAge(15);
```

Think: Blueprint (Class) → Actual House (Object)

---

### 3. **Method**
A method is an **action** that an object can do.

```java
public void setName(String name) {  // This is a method
    this.name = name;
}
```

Like: A car has methods: `start()`, `stop()`, `accelerate()`

---

### 4. **Constructor**
A constructor is a **special method** that creates an object.

```java
public Student(String name, int age) {  // Constructor
    this.name = name;
    this.age = age;
}

Student john = new Student("John", 15);  // Uses constructor
```

---

### 5. **Interface**
An interface is a **contract** - a promise to implement certain methods.

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Contract: Any class implementing this must provide these methods
}
```

Think: Interface = Promise, Class = Fulfilling the promise

---

### 6. **Annotations** (@Something)
Annotations are **instructions** for Spring/Java.

| Annotation | Meaning |
|------------|---------|
| `@Entity` | "This class represents a database table" |
| `@RestController` | "This class handles HTTP requests" |
| `@Service` | "This class contains business logic" |
| `@Repository` | "This class talks to database" |
| `@Autowired` | "Spring, please provide this object automatically" |
| `@PostMapping` | "This method handles POST requests" |
| `@GetMapping` | "This method handles GET requests" |

---

### 7. **Dependency Injection (DI)**
Instead of creating objects yourself, Spring creates and gives them to you.

**Without DI:**
```java
public class StudentController {
    private StudentService service = new StudentService();  // You create it
}
```

**With DI:**
```java
@RestController
public class StudentController {
    private final StudentService service;  // Spring gives it to you!
    
    public StudentController(StudentService service) {
        this.service = service;  // Spring injects it automatically
    }
}
```

Think: You order food (declare dependency) → Waiter brings it (Spring injects it)

---

### 8. **DTO vs Entity**

| | Entity | DTO |
|---|---|---|
| **Purpose** | Database representation | Data transfer |
| **Has ID?** | Yes (database-generated) | Depends |
| **Annotations** | `@Entity`, `@Table`, `@Id` | None (plain class) |
| **Example** | `Student.java` | `StudentRequest.java`, `StudentResponse.java` |

**Why separate?**
- User doesn't send ID when creating (database generates it)
- Database has extra fields (created_at, updated_at) user doesn't need

---

### 9. **Repository**
Repository is like a **database helper**.

You don't write SQL! Just call methods:

```java
studentRepository.save(student);           // INSERT
studentRepository.findById(1);             // SELECT WHERE id = 1
studentRepository.findAll();               // SELECT *
studentRepository.deleteById(1);           // DELETE WHERE id = 1
```

Spring JPA generates SQL automatically!

---

### 10. **Exception Handling**

Exception = Error that happens during runtime

```java
// Throwing an exception
throw new StudentNotFoundException("Student not found");

// Catching an exception
@ExceptionHandler(StudentNotFoundException.class)
public ResponseEntity<String> handleError(StudentNotFoundException ex) {
    return ResponseEntity.status(404).body(ex.getMessage());
}
```

Without exception handler → Ugly error  
With exception handler → Clean JSON error

---

### 11. **REST vs SOAP vs GraphQL**

| | REST | SOAP | GraphQL |
|---|---|---|---|
| **Protocol** | HTTP | HTTP + XML | HTTP |
| **Data Format** | JSON | XML | JSON |
| **Endpoints** | Multiple (/students, /students/1) | Single (/ws) | Single (/graphql) |
| **Flexibility** | Fixed response | Fixed response | Client chooses fields |
| **Best For** | Web/mobile apps | Enterprise systems | Mobile apps (data efficiency) |

**Example:**

REST:
```
GET /api/students/1
→ Returns: {id, name, age, grade}  (all fields)
```

GraphQL:
```
query { studentById(id: 1) { name } }
→ Returns: {name}  (only requested field!)
```

---

### 12. **How Configuration Works**

`application.properties` = Settings file

```properties
server.port=8080              # Run on port 8080
spring.datasource.url=...     # Database location
spring.jpa.show-sql=true      # Show SQL in console
```

Spring reads this file on startup and configures everything automatically!

---

## 🐛 Debugging Guide: Where to Put Breakpoints

Want to see the code execution step-by-step? Put breakpoints here:

### REST Flow Breakpoints:
1. [StudentController.java](src/main/java/com/learning/crud/controller/StudentController.java) - Line 59: `createStudent()` method start
2. [StudentService.java](src/main/java/com/learning/crud/service/StudentService.java) - Line 48: `createStudent()` method start
3. [StudentService.java](src/main/java/com/learning/crud/service/StudentService.java) - Line 56: Before `repository.save()`
4. [StudentService.java](src/main/java/com/learning/crud/service/StudentService.java) - Line 59: After `repository.save()` (see generated ID)

### SOAP Flow Breakpoints:
1. [StudentSoapEndpoint.java](src/main/java/com/learning/crud/soap/StudentSoapEndpoint.java) - Line 48: `createStudent()` method start

### GraphQL Flow Breakpoints:
1. [StudentGraphQLController.java](src/main/java/com/learning/crud/graphql/StudentGraphQLController.java) - Line 74: `createStudent()` method start

### Exception Handling Breakpoint:
1. [GlobalExceptionHandler.java](src/main/java/com/learning/crud/exception/GlobalExceptionHandler.java) - Line 32: Exception caught

**How to use breakpoints in VS Code:**
1. Click left of line number (red dot appears)
2. Run in debug mode: `F5`
3. When code hits breakpoint, execution pauses
4. Hover over variables to see their values!

---

## 📝 Learning Tasks (Practice Exercises)

Now that you understand the code, try these exercises:

### ✅ Task 1: Add GET All Students Endpoint
Add this to [StudentController.java](src/main/java/com/learning/crud/controller/StudentController.java):

```java
@GetMapping
public ResponseEntity<List<StudentResponse>> getAllStudents() {
    // 1. Call service to get all students
    // 2. Service calls repository.findAll()
    // 3. Convert List<Student> → List<StudentResponse>
    // 4. Return response
}
```

**Hint:** You'll need to add a method in [StudentService.java](src/main/java/com/learning/crud/service/StudentService.java) too!

---

### ✅ Task 2: Add Input Validation
Add validation to [StudentRequest.java](src/main/java/com/learning/crud/dto/StudentRequest.java):

```java
import jakarta.validation.constraints.*;

public class StudentRequest {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    
    @Min(value = 5, message = "Age must be at least 5")
    @Max(value = 100, message = "Age must be less than 100")
    private Integer age;
    
    @NotBlank(message = "Grade cannot be empty")
    private String grade;
}
```

Then add `@Valid` in controller:
```java
public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request)
```

---

### ✅ Task 3: Add Update Student Endpoint
Add PUT endpoint:

```java
@PutMapping("/{id}")
public ResponseEntity<StudentResponse> updateStudent(
    @PathVariable Long id,
    @RequestBody StudentRequest request) {
    // 1. Find student by ID
    // 2. Update fields
    // 3. Save back to database
    // 4. Return updated student
}
```

---

### ✅ Task 4: Add Delete Student Endpoint
Add DELETE endpoint:

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
    // 1. Call service.deleteStudent(id)
    // 2. Service calls repository.deleteById(id)
    // 3. Return 204 No Content
}
```

---

### ✅ Task 5: Add GraphQL Query for All Students
Add to [schema.graphqls](src/main/resources/graphql/schema.graphqls):

```graphql
type Query {
    studentById(id: ID!): Student
    allStudents: [Student]  # Add this line
}
```

Then add method in [StudentGraphQLController.java](src/main/java/com/learning/crud/graphql/StudentGraphQLController.java):

```java
@QueryMapping
public List<StudentResponse> allStudents() {
    // Call service to get all students
}
```

---

## 🎓 Next Steps: Keep Learning!

1. **Try the learning tasks above** - Best way to learn is by doing!

2. **Experiment with the code:**
   - Change field names
   - Add new fields (email, phone)
   - Add new entity (Course, Teacher)

3. **Learn more about:**
   - Spring Security (authentication/authorization)
   - Database relationships (One-to-Many, Many-to-Many)
   - Testing (JUnit, MockMvc)
   - Docker (containerization)
   - Deploying to cloud (AWS, Azure, Heroku)

4. **Resources:**
   - [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
   - [Baeldung](https://www.baeldung.com/) - Excellent Spring tutorials
   - [JPA/Hibernate Guide](https://www.baeldung.com/learn-jpa-hibernate)

---

## ❓ Common Issues & Solutions

### Issue 1: Port 8080 already in use
**Solution:** Change port in `application.properties`:
```properties
server.port=8081
```

### Issue 2: mvn command not found
**Solution:** Install Maven or use Maven wrapper:
```bash
./mvnw spring-boot:run    # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

### Issue 3: Java version mismatch
**Solution:** Ensure Java 17 is installed:
```bash
java -version
```

### Issue 4: Database not resetting
**Solution:** Stop application (Ctrl+C) and restart. H2 is in-memory, data is lost on restart.

---

## 🎉 Congratulations!

You now have a complete working Java application with:
- ✅ REST APIs
- ✅ SOAP APIs
- ✅ GraphQL APIs
- ✅ Database integration
- ✅ Exception handling
- ✅ Clean architecture

**Remember:** The best way to learn programming is by **reading code, modifying it, and breaking things**. Don't be afraid to experiment!

---

## 📞 Need Help?

If you're stuck:
1. Read the code comments carefully
2. Put breakpoints and debug
3. Check the console logs
4. Google the error message
5. Ask on Stack Overflow

Happy Coding! 🚀
