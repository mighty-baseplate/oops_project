package com.examsystem.service;

import com.examsystem.entity.Student;
import com.examsystem.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Student Service Implementation.
 * Demonstrates SOLID - Single Responsibility and Dependency Injection.
 */
@Service
@Transactional
public class StudentServiceImpl implements IStudentService {
    
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    
    public StudentServiceImpl(StudentRepository studentRepository,
                             PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Student registerStudent(String name, String username, String password) {
        if (studentRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        Student student = new Student(name, username, passwordEncoder.encode(password));
        return studentRepository.save(student);
    }
    
    @Override
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + username));
    }
    
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
    }
    
    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        return studentRepository.findByUsername(username)
                .map(student -> passwordEncoder.matches(password, student.getPassword()))
                .orElse(false);
    }
}
