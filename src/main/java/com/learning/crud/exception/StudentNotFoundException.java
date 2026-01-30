package com.learning.crud.exception;

/**
 * CUSTOM EXCEPTION: Thrown when student is not found
 * 
 * Exception = An error or unexpected situation
 * 
 * Why create custom exception?
 * - More descriptive error messages
 * - Better error handling
 * - Follows clean code principles
 * 
 * RuntimeException vs Exception:
 * - RuntimeException: Unchecked (compiler doesn't force you to handle it)
 * - Exception: Checked (compiler forces you to handle it)
 * 
 * We extend RuntimeException for simplicity
 */
public class StudentNotFoundException extends RuntimeException {

    /**
     * Constructor: Creates exception with custom message
     * 
     * Example usage:
     * throw new StudentNotFoundException("Student not found with id: 5");
     */
    public StudentNotFoundException(String message) {
        super(message); // Calls parent class constructor
    }
}
