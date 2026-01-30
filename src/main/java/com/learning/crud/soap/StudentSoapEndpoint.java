package com.learning.crud.soap;

import com.learning.crud.dto.StudentRequest;
import com.learning.crud.dto.StudentResponse;
import com.learning.crud.service.StudentService;
import com.learning.soap.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * SOAP ENDPOINT: Handles SOAP requests
 * 
 * This is the ENTRY POINT for SOAP API calls.
 * Similar to REST Controller, but for SOAP protocol.
 * 
 * SOAP = Simple Object Access Protocol
 * - Uses XML instead of JSON
 * - More formal and structured than REST
 * - Used in enterprise applications
 * 
 * @Endpoint tells Spring: "This class handles SOAP requests"
 */
@Endpoint
public class StudentSoapEndpoint {

    // SOAP namespace (must match XSD targetNamespace)
    private static final String NAMESPACE_URI = "http://learning.com/crud/soap";

    // Dependency Injection: Spring gives us StudentService
    private final StudentService studentService;

    public StudentSoapEndpoint(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * ========================================
     * SOAP API #1: CREATE STUDENT
     * ========================================
     * 
     * URL: POST http://localhost:8080/ws
     * 
     * @PayloadRoot: Defines which SOAP request this method handles
     * - namespace: Must match XSD targetNamespace
     * - localPart: Must match XSD element name
     * 
     * @RequestPayload: The incoming SOAP request (XML → Java object)
     * @ResponsePayload: The outgoing SOAP response (Java object → XML)
     * 
     * FLOW:
     * 1. Client sends SOAP XML request
     * 2. Spring converts XML → CreateStudentRequest object (from XSD)
     * 3. This method receives CreateStudentRequest
     * 4. Extracts data and creates StudentRequest DTO
     * 5. Calls studentService.createStudent()
     * 6. Builds CreateStudentResponse from service response
     * 7. Spring converts CreateStudentResponse → XML
     * 8. Returns XML to client
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createStudentRequest")
    @ResponsePayload
    public CreateStudentResponse createStudent(@RequestPayload CreateStudentRequest request) {
        // Convert SOAP request → DTO
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName(request.getName());
        studentRequest.setAge(request.getAge());
        studentRequest.setGrade(request.getGrade());

        // Call service layer
        StudentResponse studentResponse = studentService.createStudent(studentRequest);

        // Convert DTO → SOAP response
        CreateStudentResponse response = new CreateStudentResponse();
        Student soapStudent = new Student();
        soapStudent.setId(studentResponse.getId());
        soapStudent.setName(studentResponse.getName());
        soapStudent.setAge(studentResponse.getAge());
        soapStudent.setGrade(studentResponse.getGrade());
        response.setStudent(soapStudent);

        return response;
    }

    /**
     * ========================================
     * SOAP API #2: GET STUDENT BY ID
     * ========================================
     * 
     * URL: POST http://localhost:8080/ws
     * 
     * FLOW:
     * 1. Client sends SOAP XML with student ID
     * 2. Spring converts XML → GetStudentByIdRequest object
     * 3. Extracts ID and calls studentService.getStudentById()
     * 4. Builds GetStudentByIdResponse from service response
     * 5. Spring converts response → XML
     * 6. Returns XML to client
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getStudentByIdRequest")
    @ResponsePayload
    public GetStudentByIdResponse getStudentById(@RequestPayload GetStudentByIdRequest request) {
        // Call service layer
        StudentResponse studentResponse = studentService.getStudentById(request.getId());

        // Convert DTO → SOAP response
        GetStudentByIdResponse response = new GetStudentByIdResponse();
        Student soapStudent = new Student();
        soapStudent.setId(studentResponse.getId());
        soapStudent.setName(studentResponse.getName());
        soapStudent.setAge(studentResponse.getAge());
        soapStudent.setGrade(studentResponse.getGrade());
        response.setStudent(soapStudent);

        return response;
    }
}
