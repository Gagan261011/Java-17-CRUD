package com.learning.crud.service;

import com.learning.crud.dto.StudentRequest;
import com.learning.crud.dto.StudentResponse;
import com.learning.crud.entity.Student;
import com.learning.crud.exception.StudentNotFoundException;
import com.learning.crud.repository.StudentRepository;
import org.springframework.stereotype.Service;

/**
 * SERVICE LAYER: Contains business logic
 * 
 * Think of this as the "brain" of the application.
 * - Controller receives requests and calls Service
 * - Service processes the data and calls Repository
 * - Repository talks to database
 * 
 * @Service tells Spring: "This is a service class with business logic"
 * 
 * Why separate Service from Controller?
 * - Controller = handles HTTP/SOAP/GraphQL requests
 * - Service = contains reusable business logic
 * - Same service can be used by REST, SOAP, and GraphQL!
 */
@Service
public class StudentService {

    // Dependency Injection: Spring automatically provides StudentRepository
    private final StudentRepository studentRepository;

    /**
     * Constructor Injection (recommended way)
     * Spring sees this constructor and automatically injects StudentRepository
     */
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * CREATE: Save a new student to database
     * 
     * Flow:
     * 1. Receive StudentRequest (from controller)
     * 2. Convert DTO → Entity
     * 3. Save entity using repository
     * 4. Convert saved entity → StudentResponse
     * 5. Return response
     */
    public StudentResponse createStudent(StudentRequest request) {
        // Step 1: Create new Student entity from request
        Student student = new Student();
        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setGrade(request.getGrade());

        // Step 2: Save to database (repository.save calls JPA)
        // JPA generates SQL: INSERT INTO students (name, age, grade) VALUES (?, ?, ?)
        Student savedStudent = studentRepository.save(student);

        // Step 3: Convert entity to response DTO
        return convertToResponse(savedStudent);
    }

    /**
     * READ: Get student by ID from database
     * 
     * Flow:
     * 1. Call repository.findById()
     * 2. If found: convert to DTO and return
     * 3. If not found: throw exception
     */
    public StudentResponse getStudentById(Long id) {
        // findById() returns Optional<Student>
        // Optional is like a box that may or may not contain a value
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        // Convert entity to response DTO
        return convertToResponse(student);
    }

    /**
     * Helper method: Convert Entity → Response DTO
     * 
     * This is called "mapping" or "conversion"
     * We copy data from Student entity to StudentResponse DTO
     */
    private StudentResponse convertToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setAge(student.getAge());
        response.setGrade(student.getGrade());
        return response;
    }
}
