package com.learning.crud.dto;

/**
 * DTO (Data Transfer Object): Used to receive data from client
 * 
 * Why separate from Entity?
 * - Entity = what we save in database (has ID, database annotations)
 * - DTO = what we receive from user (no ID, no database stuff)
 * 
 * When creating a new student, user doesn't provide ID (database generates it)
 * So we use this DTO to accept: name, age, grade only
 */
public class StudentRequest {

    private String name;
    private Integer age;
    private String grade;

    // ===== CONSTRUCTORS =====
    
    public StudentRequest() {
    }

    public StudentRequest(String name, Integer age, String grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    // ===== GETTERS AND SETTERS =====
    
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

    @Override
    public String toString() {
        return "StudentRequest{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                '}';
    }
}
