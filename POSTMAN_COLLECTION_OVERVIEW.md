# 📮 Postman Collection - Visual Overview

```
╔═══════════════════════════════════════════════════════════════════╗
║                   Student CRUD API - Java 17                      ║
║                   Postman Collection v2.1                         ║
╚═══════════════════════════════════════════════════════════════════╝

📁 REST APIs (4 requests)
│
├─ 🟢 Create Student
│  │  POST http://localhost:8080/api/students
│  │  Body: {"name":"John Doe","age":15,"grade":"A"}
│  │  ✓ Auto-saves student ID to {{student_id}}
│  │  ✓ Tests: Status 201, JSON response, required fields
│  │
├─ 🔵 Get Student by ID
│  │  GET http://localhost:8080/api/students/{{student_id}}
│  │  Uses: Variable from Create Student
│  │  ✓ Tests: Status 200, JSON response, data integrity
│  │
├─ 🔴 Get Student by ID - Not Found
│  │  GET http://localhost:8080/api/students/999
│  │  Tests error handling
│  │  ✓ Tests: Status 404, error message format
│  │
└─ 🟡 Health Check
   │  GET http://localhost:8080/api/students/health
   │  Verify API is running
   │  ✓ Tests: Status 200

───────────────────────────────────────────────────────────────────

📁 SOAP APIs (3 requests)
│
├─ 🟢 Create Student (SOAP)
│  │  POST http://localhost:8080/ws
│  │  Content-Type: text/xml
│  │  Body: SOAP Envelope with createStudentRequest
│  │  Student: Jane Smith, 16, B
│  │  ✓ Tests: Status 200, XML response, SOAP envelope
│  │
├─ 🔵 Get Student by ID (SOAP)
│  │  POST http://localhost:8080/ws
│  │  Content-Type: text/xml
│  │  Body: SOAP Envelope with getStudentByIdRequest
│  │  ✓ Tests: Status 200, XML response, student data
│  │
└─ 📄 Get WSDL
   │  GET http://localhost:8080/ws/students.wsdl
   │  Returns: WSDL document describing SOAP service

───────────────────────────────────────────────────────────────────

📁 GraphQL APIs (3 requests)
│
├─ 🟢 Create Student (Mutation)
│  │  POST http://localhost:8080/graphql
│  │  Query: mutation { createStudent(...) { id name age grade } }
│  │  Student: Alice Johnson, 17, A
│  │  ✓ Auto-saves student ID to {{graphql_student_id}}
│  │  ✓ Tests: Status 200, data field, student created
│  │
├─ 🔵 Get Student by ID (Query)
│  │  POST http://localhost:8080/graphql
│  │  Query: query { studentById(id: 1) { id name age grade } }
│  │  Returns: All requested fields
│  │  ✓ Tests: Status 200, data field, student exists
│  │
└─ 🔷 Get Student - Only Name (Selective)
   │  POST http://localhost:8080/graphql
   │  Query: query { studentById(id: 1) { name } }
   │  Demonstrates: GraphQL's selective field fetching
   │  Returns: ONLY name field (no age, grade)
   │  ✓ Tests: Only requested fields present

───────────────────────────────────────────────────────────────────

📁 Utilities (2 browser links)
│
├─ 🌐 H2 Console (Browser)
│  │  GET http://localhost:8080/h2-console
│  │  Opens: H2 Database web console
│  │  Use: View tables, run SQL, verify data
│  │  Login: jdbc:h2:mem:testdb / sa / (no password)
│  │
└─ 🌐 GraphiQL Playground (Browser)
   │  GET http://localhost:8080/graphiql
   │  Opens: Interactive GraphQL IDE
   │  Use: Test queries, explore schema, auto-complete

═══════════════════════════════════════════════════════════════════

COLLECTION VARIABLES:
├─ {{base_url}}             = http://localhost:8080
├─ {{student_id}}           = (auto-set by REST Create Student)
└─ {{graphql_student_id}}   = (auto-set by GraphQL Create Student)

═══════════════════════════════════════════════════════════════════

FEATURES:
✅ 12 Total Requests
✅ Automatic Tests on Every Request
✅ Auto-saves Student IDs
✅ Detailed Documentation
✅ Pre-configured Headers
✅ Request Examples
✅ Global Scripts (logging)

═══════════════════════════════════════════════════════════════════

TESTING SEQUENCE (Recommended):
1. Health Check                    → Verify API is running
2. Create Student (REST)           → Creates John Doe
3. Get Student by ID (REST)        → Gets John Doe
4. Create Student (SOAP)           → Creates Jane Smith
5. Get Student by ID (SOAP)        → Gets Jane Smith
6. Create Student (GraphQL)        → Creates Alice Johnson
7. Get Student by ID (GraphQL)     → Gets Alice Johnson
8. Get Student - Only Name         → Gets only name field
9. Get Student - Not Found         → Tests error handling
10. H2 Console                     → View all students in DB
11. GraphiQL Playground            → Interactive GraphQL

═══════════════════════════════════════════════════════════════════

COMPARISON VIEW:

┌─────────────┬─────────────┬─────────────┬─────────────┐
│   Feature   │    REST     │    SOAP     │   GraphQL   │
├─────────────┼─────────────┼─────────────┼─────────────┤
│ Requests    │      4      │      3      │      3      │
│ Format      │    JSON     │     XML     │    JSON     │
│ Endpoint    │  /api/...   │     /ws     │  /graphql   │
│ Methods     │ POST, GET   │    POST     │    POST     │
│ Tests       │     ✓       │     ✓       │     ✓       │
│ Variables   │     ✓       │     -       │     ✓       │
│ Docs        │     ✓       │     ✓       │     ✓       │
└─────────────┴─────────────┴─────────────┴─────────────┘

═══════════════════════════════════════════════════════════════════

QUICK IMPORT:
1. Open Postman
2. Click "Import" (top left)
3. Select "Student-CRUD-API.postman_collection.json"
4. Click "Import"
5. Start Testing! 🚀

═══════════════════════════════════════════════════════════════════
```

## 📊 Request Details

### REST - Create Student
```http
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "name": "John Doe",
  "age": 15,
  "grade": "A"
}
```
**Response (201 Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "age": 15,
  "grade": "A"
}
```

---

### SOAP - Create Student
```xml
POST http://localhost:8080/ws
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
**Response (200 OK):**
```xml
<SOAP-ENV:Envelope>
   <SOAP-ENV:Body>
      <ns2:createStudentResponse>
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

### GraphQL - Create Student
```http
POST http://localhost:8080/graphql
Content-Type: application/json

{
  "query": "mutation { createStudent(name: \"Alice Johnson\", age: 17, grade: \"A\") { id name age grade } }"
}
```
**Response (200 OK):**
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

## 🎯 Usage Tips

### Tip 1: Run All Tests
Right-click "REST APIs" folder → **Run folder**
- Runs all 4 REST requests in sequence
- Shows summary: ✓ passed / ✗ failed
- Great for regression testing!

### Tip 2: Use Collection Runner
1. Click collection name
2. Click **Run** button
3. Select requests to run
4. Set iterations (run multiple times)
5. Click **Run Student CRUD API**
6. View detailed results

### Tip 3: Save Responses
After running request:
1. Click **Save Response**
2. Save as example
3. Useful for documentation!

### Tip 4: Monitor Console
**View → Show Postman Console**
- See all requests/responses
- Debug issues
- View custom logs

---

## 📖 Documentation in Postman

Each request has documentation! To view:
1. Click request name
2. Look for **📄 icon** on right
3. Click to see detailed docs
4. Includes:
   - What it does
   - Parameters
   - Response format
   - Usage examples

---

**Happy Testing with Postman! 🚀📮**
