package com.learning.crud.repository;

import com.learning.crud.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * REPOSITORY: Talks to the database
 * 
 * Think of this as a "database helper" that does all SQL operations for you.
 * You DON'T write SQL code! Spring JPA does it automatically.
 * 
 * @Repository tells Spring: "This is a database access class"
 * 
 * JpaRepository<Student, Long> means:
 * - Student: The entity we're working with
 * - Long: The type of the ID field in Student
 * 
 * By extending JpaRepository, we automatically get these methods:
 * - save(student)         → INSERT INTO students...
 * - findById(id)          → SELECT * FROM students WHERE id = ?
 * - findAll()             → SELECT * FROM students
 * - deleteById(id)        → DELETE FROM students WHERE id = ?
 * - count()               → SELECT COUNT(*) FROM students
 * 
 * This is called "Repository Pattern" - separates database logic from business logic
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // No code needed here! JpaRepository provides all basic CRUD methods.
    
    // You can add custom queries if needed, examples:
    // List<Student> findByName(String name);
    // List<Student> findByGrade(String grade);
    // List<Student> findByAgeGreaterThan(Integer age);
    
}
