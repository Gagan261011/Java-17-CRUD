# 📮 Postman Collection Guide

## 🎯 Quick Import

1. **Open Postman** (Download from https://www.postman.com/downloads/ if needed)

2. **Import the Collection:**
   - Click **Import** button (top left)
   - Select **File** tab
   - Choose: `Student-CRUD-API.postman_collection.json`
   - Click **Import**

3. **Done!** You'll see "Student CRUD API - Java 17" in your collections

---

## 🚀 Using the Collection

### Step 1: Start the Application
```bash
cd "e:\Advanced Projects\Java-17-CRUD"
mvn spring-boot:run
```

Wait for: `🚀 Application Started Successfully!`

### Step 2: Test in Order

**Recommended Testing Sequence:**

1. **Health Check** (REST APIs → Health Check)
   - Verifies API is running
   - Should return: "REST API is running! ✓"

2. **Create Student - REST** (REST APIs → Create Student)
   - Creates a new student: John Doe
   - Returns student with ID
   - **Auto-saves ID** to variable `{{student_id}}`

3. **Get Student by ID - REST** (REST APIs → Get Student by ID)
   - Uses `{{student_id}}` from previous request
   - Returns student details

4. **Create Student - SOAP** (SOAP APIs → Create Student)
   - Creates student: Jane Smith
   - Returns XML response

5. **Get Student by ID - SOAP** (SOAP APIs → Get Student by ID)
   - Retrieves student via SOAP
   - Returns XML with student data

6. **Create Student - GraphQL** (GraphQL APIs → Create Student Mutation)
   - Creates student: Alice Johnson
   - Returns only requested fields
   - **Auto-saves ID** to `{{graphql_student_id}}`

7. **Get Student - GraphQL** (GraphQL APIs → Get Student by ID Query)
   - Retrieves student data
   - Shows GraphQL flexibility

8. **Get Student - Selective** (GraphQL APIs → Get Student - Only Name)
   - Returns ONLY the name field
   - Demonstrates GraphQL's power!

---

## 📊 Collection Structure

```
Student CRUD API - Java 17
│
├── 📁 REST APIs (4 requests)
│   ├── Create Student
│   ├── Get Student by ID
│   ├── Get Student by ID - Not Found (error handling)
│   └── Health Check
│
├── 📁 SOAP APIs (3 requests)
│   ├── Create Student (SOAP)
│   ├── Get Student by ID (SOAP)
│   └── Get WSDL
│
├── 📁 GraphQL APIs (3 requests)
│   ├── Create Student (Mutation)
│   ├── Get Student by ID (Query)
│   └── Get Student - Only Name (Selective)
│
└── 📁 Utilities (2 browser links)
    ├── H2 Console (Browser)
    └── GraphiQL Playground (Browser)
```

**Total: 12 requests**

---

## 🎨 Features Included

### ✅ Automatic Tests
Each request includes automated tests that verify:
- ✓ Correct HTTP status code
- ✓ Response format (JSON/XML)
- ✓ Required fields present
- ✓ Data integrity

Check the **Test Results** tab after each request!

### ✅ Variables
Collection uses variables for flexibility:

| Variable | Purpose | Auto-Set |
|----------|---------|----------|
| `{{base_url}}` | Server URL (http://localhost:8080) | Manual |
| `{{student_id}}` | Last created student ID (REST) | ✓ Auto |
| `{{graphql_student_id}}` | Last created student ID (GraphQL) | ✓ Auto |

**Edit Variables:**
1. Click collection name
2. Go to **Variables** tab
3. Modify as needed

### ✅ Detailed Documentation
Every request includes:
- Description of what it does
- Request format explanation
- Response format details
- Usage examples

Click the **📄 icon** next to request name to view documentation.

### ✅ Pre-configured Headers
All headers are set automatically:
- REST: `Content-Type: application/json`
- SOAP: `Content-Type: text/xml`
- GraphQL: `Content-Type: application/json`

---

## 🧪 Testing Scenarios

### Scenario 1: Complete CRUD Flow (REST)
1. Create Student
2. Get Student by ID
3. Verify in H2 Console

### Scenario 2: Error Handling
1. Get Student by ID - Not Found
2. Check error response format
3. Verify HTTP 404 status

### Scenario 3: Compare APIs
Create same student using:
1. REST API
2. SOAP API
3. GraphQL API

Then view all in H2 Console!

### Scenario 4: GraphQL Flexibility
Run these queries on same student:
1. Get all fields
2. Get only name
3. Get only age and grade

See how GraphQL returns different data!

---

## 💡 Pro Tips

### 1. Use Environments
Create different environments for different servers:
- **Local**: http://localhost:8080
- **Dev**: http://dev-server:8080
- **Staging**: http://staging-server:8080

### 2. View Console Logs
Open **Postman Console** (View → Show Postman Console) to see:
- Request details
- Response timing
- Custom log messages

### 3. Run Entire Folder
Right-click any folder → **Run folder**
- Tests all requests in sequence
- Shows success/fail summary
- Great for regression testing!

### 4. Export Results
After running tests:
1. Click **Runner** results
2. Click **Export Results**
3. Save as JSON for documentation

### 5. Modify Requests
Try modifying:
- Student names
- Ages and grades
- GraphQL query fields
- SOAP XML structure

Experiment and learn!

---

## 🔧 Troubleshooting

### Issue: "Could not get any response"
**Solution:** 
- Ensure application is running: `mvn spring-boot:run`
- Check port 8080 is not in use
- Verify URL: http://localhost:8080

### Issue: "404 Not Found"
**Solution:**
- Check endpoint URL is correct
- For GET by ID: Create a student first
- Verify application started successfully

### Issue: "500 Internal Server Error"
**Solution:**
- Check console for error logs
- Verify request body format is correct
- Check required fields are provided

### Issue: Variables not working ({{student_id}})
**Solution:**
- Run "Create Student" request first (sets the variable)
- Or manually set variable value in collection settings
- Or replace `{{student_id}}` with actual ID like `1`

---

## 📱 Browser-Based Tools

### H2 Console
**URL:** http://localhost:8080/h2-console

**How to Access:**
1. Click "H2 Console (Browser)" in Utilities folder
2. Postman opens browser
3. Enter connection details:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)
4. Click Connect

**Use For:**
- View STUDENTS table
- Run SQL queries
- Verify CRUD operations

### GraphiQL Playground
**URL:** http://localhost:8080/graphiql

**How to Access:**
1. Click "GraphiQL Playground (Browser)" in Utilities folder
2. Interactive GraphQL IDE opens
3. Try queries and mutations
4. See schema documentation

**Use For:**
- Learning GraphQL syntax
- Testing queries interactively
- Auto-completion help
- Schema exploration

---

## 📋 Quick Reference

### REST Endpoints
```
POST   http://localhost:8080/api/students       Create
GET    http://localhost:8080/api/students/{id}  Read
```

### SOAP Endpoint
```
POST   http://localhost:8080/ws                 All operations
GET    http://localhost:8080/ws/students.wsdl   WSDL
```

### GraphQL Endpoint
```
POST   http://localhost:8080/graphql            All operations
GET    http://localhost:8080/graphiql           Playground
```

---

## 🎓 Learning Exercise

**Challenge:** Create a complete test suite!

1. Create 3 students (one via each API type)
2. Retrieve each student
3. Verify all exist in H2 Console
4. Try to get non-existent student (ID: 999)
5. Export your test results

**Time:** 10 minutes
**Difficulty:** Beginner

---

## 📞 Need Help?

- **Postman Docs:** https://learning.postman.com/
- **Project README:** [README.md](README.md)
- **Sample Requests:** [SAMPLE_REQUESTS.txt](SAMPLE_REQUESTS.txt)

---

**Happy Testing! 🚀**
