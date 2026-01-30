package com.learning.crud.graphql;

import com.learning.crud.dto.StudentRequest;
import com.learning.crud.dto.StudentResponse;
import com.learning.crud.service.StudentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GRAPHQL CONTROLLER: Handles GraphQL requests
 * 
 * This is the ENTRY POINT for GraphQL API calls.
 * Similar to REST Controller, but for GraphQL protocol.
 * 
 * GraphQL Benefits:
 * - Client decides what data they want (no over-fetching or under-fetching)
 * - Single endpoint for all operations
 * - Strongly typed schema
 * - Great for mobile apps (reduces data transfer)
 * 
 * @Controller tells Spring: "This class handles requests"
 * (We don't use @RestController because GraphQL has its own response handling)
 */
@Controller
public class StudentGraphQLController {

    // Dependency Injection: Spring gives us StudentService
    private final StudentService studentService;

    public StudentGraphQLController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * ========================================
     * GRAPHQL QUERY #1: GET STUDENT BY ID
     * ========================================
     * 
     * URL: POST http://localhost:8080/graphql
     * 
     * @QueryMapping: Maps to a query defined in schema.graphqls
     * @Argument: Extracts parameter from GraphQL query
     * 
     * Example GraphQL Query:
     * {
     *   studentById(id: 1) {
     *     id
     *     name
     *     age
     *     grade
     *   }
     * }
     * 
     * Client can choose which fields to fetch:
     * - Want only name? Request only name
     * - Want all fields? Request all fields
     * 
     * FLOW:
     * 1. Client sends GraphQL query with ID
     * 2. Spring GraphQL parses the query
     * 3. Calls this method with id parameter
     * 4. Calls studentService.getStudentById()
     * 5. Returns StudentResponse
     * 6. Spring GraphQL converts response to JSON
     * 7. Returns only the fields client requested
     */
    @QueryMapping
    public StudentResponse studentById(@Argument Long id) {
        return studentService.getStudentById(id);
    }

    /**
     * ========================================
     * GRAPHQL MUTATION #1: CREATE STUDENT
     * ========================================
     * 
     * URL: POST http://localhost:8080/graphql
     * 
     * @MutationMapping: Maps to a mutation defined in schema.graphqls
     * @Argument: Extracts parameters from GraphQL mutation
     * 
     * Example GraphQL Mutation:
     * mutation {
     *   createStudent(name: "John Doe", age: 15, grade: "A") {
     *     id
     *     name
     *     age
     *     grade
     *   }
     * }
     * 
     * FLOW:
     * 1. Client sends GraphQL mutation with student data
     * 2. Spring GraphQL parses the mutation
     * 3. Calls this method with individual parameters
     * 4. Creates StudentRequest DTO
     * 5. Calls studentService.createStudent()
     * 6. Returns StudentResponse
     * 7. Spring GraphQL converts response to JSON
     * 8. Returns only the fields client requested
     */
    @MutationMapping
    public StudentResponse createStudent(
            @Argument String name,
            @Argument Integer age,
            @Argument String grade) {
        
        // Create StudentRequest from individual parameters
        StudentRequest request = new StudentRequest();
        request.setName(name);
        request.setAge(age);
        request.setGrade(grade);

        // Call service layer
        return studentService.createStudent(request);
    }
}
