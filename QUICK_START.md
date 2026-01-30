# 🚀 QUICK START GUIDE

## First Time Setup (5 minutes)

### Step 1: Verify Prerequisites
```bash
# Check Java version (must be 17)
java -version

# Check Maven version
mvn -version
```

If not installed:
- Java 17: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Maven: https://maven.apache.org/download.cgi

### Step 2: Build and Run
```bash
# Navigate to project directory
cd "e:\Advanced Projects\Java-17-CRUD"

# Clean and compile (generates SOAP classes from XSD)
mvn clean compile

# Run the application
mvn spring-boot:run
```

### Step 3: Verify Application Started
Look for this message in console:
```
🚀 Application Started Successfully!
📍 REST API: http://localhost:8080/api/students
📍 SOAP WSDL: http://localhost:8080/ws/students.wsdl
📍 GraphQL: http://localhost:8080/graphql
📍 H2 Console: http://localhost:8080/h2-console
```

### Step 4: Test an Endpoint

**Option A: Using Postman (Recommended)**
1. Open Postman
2. Import: `Student-CRUD-API.postman_collection.json`
3. Run any request!
4. See [POSTMAN_GUIDE.md](POSTMAN_GUIDE.md) for details

**Option B: Using Command Line**

Open new terminal (keep app running) and run:

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/students" -Method Post -ContentType "application/json" -Body '{"name":"John Doe","age":15,"grade":"A"}'
```

**Curl:**
```bash
curl -X POST http://localhost:8080/api/students -H "Content-Type: application/json" -d '{"name":"John Doe","age":15,"grade":"A"}'
```

Expected Response:
```json
{
  "id": 1,
  "name": "John Doe",
  "age": 15,
  "grade": "A"
}
```

## 🎯 Quick Testing Checklist

- [ ] **Import Postman Collection** (Student-CRUD-API.postman_collection.json)
- [ ] REST API: Create student
- [ ] REST API: Get student by ID
- [ ] SOAP API: Create student (see SAMPLE_REQUESTS.txt or Postman)
- [ ] SOAP API: Get student by ID
- [ ] GraphQL: Create student
- [ ] GraphQL: Query student by ID
- [ ] Open H2 Console: http://localhost:8080/h2-console
- [ ] Open GraphiQL: http://localhost:8080/graphiql

**💡 Tip:** Use Postman collection for easiest testing experience!

## 📁 Project Files Overview

| File | Purpose |
|------|---------|
| `pom.xml` | Maven dependencies configuration |
| `README.md` | Complete beginner's guide |
| `SAMPLE_REQUESTS.txt` | Copy-paste test requests |
| `Student-CRUD-API.postman_collection.json` | **Postman collection (import this!)** |
| `POSTMAN_GUIDE.md` | **How to use Postman collection** |
| `THIS_FILE.md` | Quick start guide |
| `src/main/java/.../CrudApplication.java` | Application entry point |
| `src/main/java/.../entity/Student.java` | Database entity |
| `src/main/java/.../controller/StudentController.java` | REST endpoints |
| `src/main/java/.../soap/StudentSoapEndpoint.java` | SOAP endpoints |
| `src/main/java/.../graphql/StudentGraphQLController.java` | GraphQL endpoints |
| `src/main/java/.../service/StudentService.java` | Business logic |
| `src/main/java/.../repository/StudentRepository.java` | Database access |
| `src/main/resources/application.properties` | App configuration |
| `src/main/resources/graphql/schema.graphqls` | GraphQL schema |
| `src/main/resources/xsd/students.xsd` | SOAP schema |

## 🐛 Troubleshooting

### Problem: "Port 8080 already in use"
**Solution:** Change port in `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Problem: "mvn: command not found"
**Solution:** Add Maven to PATH or download from https://maven.apache.org/download.cgi

### Problem: "Error: Could not find or load main class"
**Solution:** Run `mvn clean compile` first to generate SOAP classes

### Problem: Application starts but endpoints don't work
**Solution:** 
1. Check console for errors
2. Verify application started successfully
3. Check if port 8080 is accessible: `curl http://localhost:8080/api/students/health`

### Problem: SOAP classes not found
**Solution:** Run `mvn clean compile` to generate classes from XSD schema

## 📚 Learning Path

1. **Start Here:** Read [README.md](README.md) - Complete beginner's guide
2. **Try It:** Use [SAMPLE_REQUESTS.txt](SAMPLE_REQUESTS.txt) to test all endpoints
3. **Understand Flow:** Follow the "Flow Explanation" section in README
4. **Debug:** Put breakpoints as shown in README's debugging guide
5. **Practice:** Complete the learning tasks in README
6. **Experiment:** Modify code and see what happens!

## 🎓 Key Concepts to Master

- [ ] What is a REST API?
- [ ] What is SOAP?
- [ ] What is GraphQL?
- [ ] How does Spring Boot work?
- [ ] What is dependency injection?
- [ ] What is JPA/Hibernate?
- [ ] What is an Entity vs DTO?
- [ ] How does exception handling work?
- [ ] What is H2 database?
- [ ] How to read stack traces?

All explained in [README.md](README.md) in simple words!

## 💡 Tips for Learning

1. **Run the app first** - See it working before diving into code
2. **Put breakpoints** - See execution flow step by step
3. **Read comments** - Every file has detailed explanations
4. **Modify code** - Best way to learn is by breaking and fixing
5. **Check logs** - Console shows SQL queries and HTTP requests
6. **Use H2 console** - See actual database content
7. **Try GraphiQL** - Interactive GraphQL playground

## 🔥 Next Steps

After mastering this project:
1. Add UPDATE and DELETE endpoints
2. Add input validation
3. Add unit tests (JUnit)
4. Add Spring Security
5. Connect to PostgreSQL/MySQL instead of H2
6. Add pagination and sorting
7. Add Swagger/OpenAPI documentation
8. Deploy to cloud (Heroku, AWS, Azure)

## 📞 Resources

- **This Project's README:** [README.md](README.md) - Ultra beginner friendly
- **Sample Requests:** [SAMPLE_REQUESTS.txt](SAMPLE_REQUESTS.txt)
- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **Baeldung Tutorials:** https://www.baeldung.com/
- **Stack Overflow:** https://stackoverflow.com/

---

**Remember:** Programming is learned by DOING, not just reading. Run the code, break it, fix it, and experiment!

Happy Learning! 🚀
