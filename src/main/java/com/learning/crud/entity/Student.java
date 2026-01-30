package com.learning.crud.entity;

import jakarta.persistence.*;

/**
 * ENTITY CLASS: Represents a database table
 * 
 * Think of this as a blueprint for the "students" table in the database.
 * Each object of this class = one row in the table.
 * Each field = one column in the table.
 * 
 * @Entity tells JPA: "This class should be saved in database"
 * @Table specifies the table name
 */
@Entity
@Table(name = "students")
public class Student {

    /**
     * @Id: This field is the primary key (unique identifier)
     * @GeneratedValue: Database will auto-generate this number
     * 
     * Think of id like a student roll number - unique for each student
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column: Maps this field to a database column
     * nullable = false means this field is required (cannot be null)
     */
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String grade;

    // ===== CONSTRUCTORS =====
    
    /**
     * Default constructor (no parameters)
     * JPA requires this to create objects when reading from database
     */
    public Student() {
    }

    /**
     * Constructor with parameters
     * Used when we create a new student object manually
     */
    public Student(String name, Integer age, String grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    // ===== GETTERS AND SETTERS =====
    // These methods allow reading and writing the private fields
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * toString() method: Returns a string representation of the object
     * Useful for debugging and printing
     */
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                '}';
    }
}
