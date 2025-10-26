package com.examsystem.service;

import com.examsystem.entity.Student;
import java.util.List;

/**
 * Service interface for Student operations.
 * Demonstrates SOLID - Interface Segregation Principle.
 */
public interface IStudentService {
    
    /**
     * Register a new student.
     */
    Student registerStudent(String name, String username, String password);
    
    /**
     * Find student by username.
     */
    Student findByUsername(String username);
    
    /**
     * Get all students.
     */
    List<Student> getAllStudents();
    
    /**
     * Get student by ID.
     */
    Student getStudentById(Long id);
    
    /**
     * Save/Update student.
     */
    Student saveStudent(Student student);
    
    /**
     * Authenticate student.
     */
    boolean authenticate(String username, String password);
}
