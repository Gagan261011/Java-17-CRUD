package com.learning.crud.controller;

import com.learning.crud.dto.StudentRequest;
import com.learning.crud.dto.StudentResponse;
import com.learning.crud.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST CONTROLLER: Handles HTTP requests
 * 
 * This is the ENTRY POINT for REST API calls.
 * When someone calls: POST http://localhost:8080/api/students
 * This controller receives the request.
 * 
 * @RestController = @Controller + @ResponseBody
 * Tells Spring: "This class handles HTTP requests and returns JSON/XML"
 * 
 * @RequestMapping("/api/students") 
 * Sets base URL for all methods in this controller
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    // Dependency Injection: Spring gives us StudentService automatically
    private final StudentService studentService;

    /**
     * Constructor: Spring injects StudentService here
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * ========================================
     * REST API #1: CREATE STUDENT
     * ========================================
     * 
     * URL: POST http://localhost:8080/api/students
     * 
     * @PostMapping means this method handles POST requests
     * @RequestBody tells Spring to convert JSON → StudentRequest object
     * 
     * Example Request (JSON):
     * {
     *   "name": "John Doe",
     *   "age": 15,
     *   "grade": "A"
     * }
     * 
     * FLOW:
     * 1. Client sends JSON in HTTP request body
     * 2. Spring converts JSON → StudentRequest object (automatic!)
     * 3. This method receives StudentRequest object
     * 4. Calls studentService.createStudent()
     * 5. Service returns StudentResponse
     * 6. Spring converts StudentResponse → JSON (automatic!)
     * 7. Returns JSON to client with status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest request) {
        // Call service layer to create student
        StudentResponse response = studentService.createStudent(request);
        
        // Return response with HTTP status 201 (Created)
        // ResponseEntity lets us control HTTP status code
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * ========================================
     * REST API #2: GET STUDENT BY ID
     * ========================================
     * 
     * URL: GET http://localhost:8080/api/students/1
     * 
     * @GetMapping("/{id}") means this method handles GET requests
     * {id} is a path variable (dynamic part of URL)
     * @PathVariable tells Spring to extract {id} from URL
     * 
     * Example: GET /api/students/5
     * → id parameter will be 5
     * 
     * FLOW:
     * 1. Client sends GET request with ID in URL
     * 2. Spring extracts ID from URL path
     * 3. This method receives ID as parameter
     * 4. Calls studentService.getStudentById()
     * 5. Service returns StudentResponse (or throws exception if not found)
     * 6. Spring converts StudentResponse → JSON
     * 7. Returns JSON to client with status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        // Call service layer to get student
        StudentResponse response = studentService.getStudentById(id);
        
        // Return response with HTTP status 200 (OK)
        return ResponseEntity.ok(response);
    }

    /**
     * BONUS: Simple health check endpoint
     * URL: GET http://localhost:8080/api/students/health
     * Just to verify the API is running
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("REST API is running! ✓");
    }
}
