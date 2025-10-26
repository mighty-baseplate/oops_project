package com.examsystem.entity;

import com.examsystem.model.Role;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Student entity demonstrating Encapsulation with private fields.
 * Uses static counter for tracking total students (OOP concept).
 * Serializable for file I/O operations.
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Static counter for total students (shared across all instances)
    private static int studentCounter = 0;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password; // Will be encrypted
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.STUDENT;
    
    // Transient field - not persisted, used for in-memory score tracking
    @Transient
    private Map<Long, Integer> examScores = new HashMap<>();
    
    @Column
    private Integer lastExamScore;
    
    @Column
    private Long currentExamId;
    
    /**
     * Constructor that increments static counter.
     * Demonstrates static field usage in OOP.
     */
    public Student(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = Role.STUDENT;
        studentCounter++;
    }
    
    /**
     * Get total number of students created (static method).
     */
    public static int getTotalStudents() {
        return studentCounter;
    }
    
    /**
     * Reset counter (for testing purposes).
     */
    public static void resetCounter() {
        studentCounter = 0;
    }
    
    /**
     * Add score for a specific exam.
     */
    public void addExamScore(Long examId, Integer score) {
        this.examScores.put(examId, score);
        this.lastExamScore = score;
        this.currentExamId = examId;
    }
    
    /**
     * Get score for a specific exam.
     */
    public Integer getScoreForExam(Long examId) {
        return examScores.getOrDefault(examId, 0);
    }
}
