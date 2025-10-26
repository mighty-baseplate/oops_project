package com.examsystem.entity;

import com.examsystem.model.ExamType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * Coding Exam subclass demonstrating Inheritance.
 * Requires manual grading - returns placeholder score.
 */
@Entity
@DiscriminatorValue("CODING")
@NoArgsConstructor
public class CodingExam extends Exam {
    
    private static final long serialVersionUID = 1L;
    
    public CodingExam(String title, int sections, int questionsPerSection) {
        super(title, ExamType.CODING, sections, questionsPerSection);
    }
    
    /**
     * Coding exams require manual evaluation.
     * Returns 0 as placeholder - admin must grade manually.
     * Demonstrates polymorphism with different behavior.
     */
    @Override
    public int evaluate(String[] answers) {
        System.out.println("[MANUAL GRADING REQUIRED] Coding exam submitted for review.");
        System.out.println("Answers: " + (answers != null ? answers.length : 0) + " code submissions");
        // In real system, this would trigger admin notification
        return 0; // Placeholder - requires manual grading
    }
    
    @Override
    public String toString() {
        return String.format("CodingExam[id=%d, title='%s', questions=%d, manualGrading=true]",
                           getId(), getTitle(), getQuestions().size());
    }
}
