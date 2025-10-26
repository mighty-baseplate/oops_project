package com.examsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

/**
 * Question entity demonstrating Encapsulation.
 * Private fields with public getters/setters.
 * Serializable for persistence and file I/O.
 */
@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String text;
    
    @Column(length = 2000)
    private String optionsJson; // Stored as JSON string: ["opt1", "opt2", "opt3", "opt4"]
    
    @Column(nullable = false)
    private String correctAnswer;
    
    @Column(nullable = false)
    private int section; // Section number in exam
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;
    
    /**
     * Constructor for creating a question with options array.
     * Demonstrates constructor overloading.
     */
    public Question(String text, String[] options, String correctAnswer) {
        this.text = text;
        this.optionsJson = arrayToJson(options);
        this.correctAnswer = correctAnswer;
    }
    
    /**
     * Converts options array to JSON string.
     */
    private String arrayToJson(String[] options) {
        if (options == null || options.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < options.length; i++) {
            sb.append("\"").append(options[i].replace("\"", "\\\"")).append("\"");
            if (i < options.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Converts JSON string back to options array.
     */
    public String[] getOptionsArray() {
        if (optionsJson == null || optionsJson.isEmpty()) return new String[0];
        String cleaned = optionsJson.replace("[", "").replace("]", "").replace("\"", "");
        if (cleaned.isEmpty()) return new String[0];
        return cleaned.split(",");
    }
}
