package com.learning.crud.dto;

/**
 * DTO (Data Transfer Object): Used to send data back to client
 * 
 * After we save/retrieve a student from database,
 * we convert the Entity to this DTO before sending response.
 * 
 * This includes all fields including ID (because database generated it)
 */
public class StudentResponse {

    private Long id;
    private String name;
    private Integer age;
    private String grade;

    // ===== CONSTRUCTORS =====
    
    public StudentResponse() {
    }

    public StudentResponse(Long id, String name, Integer age, String grade) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    // ===== GETTERS AND SETTERS =====
    
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

    @Override
    public String toString() {
        return "StudentResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                '}';
    }
}
