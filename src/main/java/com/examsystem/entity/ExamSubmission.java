package com.examsystem.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity to store student exam submissions with answers.
 * Persists to database to survive application restarts.
 */
@Entity
@Table(name = "exam_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(nullable = false)
    private Integer score;
    
    @Column(columnDefinition = "TEXT")
    private String answersJson; // Store answers as JSON string
    
    @Column(nullable = false)
    private Boolean graded = false;
    
    /**
     * Convert String[] answers to JSON format for storage.
     */
    public void setAnswers(String[] answers) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < answers.length; i++) {
            String answer = answers[i] != null ? answers[i].replace("\"", "\\\"") : "";
            json.append("\"").append(answer).append("\"");
            if (i < answers.length - 1) {
                json.append(",");
            }
        }
        json.append("]");
        this.answersJson = json.toString();
    }
    
    /**
     * Parse JSON string back to String[] answers.
     */
    public String[] getAnswers() {
        if (answersJson == null || answersJson.isEmpty()) {
            return new String[0];
        }
        
        // Simple JSON parsing for array of strings
        String content = answersJson.substring(1, answersJson.length() - 1); // Remove [ ]
        if (content.isEmpty()) {
            return new String[0];
        }
        
        String[] parts = content.split("\",\"");
        String[] answers = new String[parts.length];
        
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            // Remove leading/trailing quotes
            part = part.replaceAll("^\"|\"$", "");
            // Unescape quotes
            part = part.replace("\\\"", "\"");
            answers[i] = part;
        }
        
        return answers;
    }
}
