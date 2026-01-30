package com.learning.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GLOBAL EXCEPTION HANDLER: Catches exceptions and returns proper error responses
 * 
 * Without this class:
 * - Exceptions would return ugly stack traces
 * - HTTP status would be 500 (Internal Server Error) for everything
 * 
 * With this class:
 * - Exceptions return clean JSON error messages
 * - Correct HTTP status codes (404, 400, etc.)
 * 
 * @RestControllerAdvice tells Spring:
 * "This class handles exceptions for all controllers"
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles StudentNotFoundException
     * 
     * @ExceptionHandler: Tells Spring which exception this method handles
     * 
     * When studentService.getStudentById() throws StudentNotFoundException:
     * 1. This method catches it
     * 2. Builds error response JSON
     * 3. Returns HTTP 404 (Not Found)
     * 
     * FLOW:
     * Controller → Service → throws Exception → This handler catches it
     */
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFound(StudentNotFoundException ex) {
        // Create error response as a Map (will be converted to JSON)
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "Not Found");
        errorResponse.put("message", ex.getMessage());

        // Return response with HTTP 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles all other unexpected exceptions
     * 
     * This is a safety net for any error we didn't specifically handle
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
