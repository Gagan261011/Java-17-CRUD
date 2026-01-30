package com.learning.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * THIS IS THE STARTING POINT OF THE ENTIRE APPLICATION
 * 
 * When you run: mvn spring-boot:run
 * Java looks for this class with main() method and starts here.
 * 
 * @SpringBootApplication tells Spring to:
 * 1. Auto-configure everything (database, web server, etc.)
 * 2. Scan all classes in this package and sub-packages
 * 3. Set up REST, SOAP, GraphQL endpoints
 */
@SpringBootApplication
public class CrudApplication {

    /**
     * main() method: The entry point of any Java program
     * When you run the app, this method executes FIRST
     */
    public static void main(String[] args) {
        // SpringApplication.run() starts the Spring Boot application
        // It starts an embedded Tomcat web server on port 8080
        SpringApplication.run(CrudApplication.class, args);
        
        System.out.println("\n==============================================");
        System.out.println("🚀 Application Started Successfully!");
        System.out.println("==============================================");
        System.out.println("📍 REST API: http://localhost:8080/api/students");
        System.out.println("📍 SOAP WSDL: http://localhost:8080/ws/students.wsdl");
        System.out.println("📍 GraphQL: http://localhost:8080/graphql");
        System.out.println("📍 H2 Console: http://localhost:8080/h2-console");
        System.out.println("==============================================\n");
    }
}
