package com.examsystem.entity;

import com.examsystem.model.ExamType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * Essay Exam subclass demonstrating Inheritance.
 * Requires manual grading by admin.
 */
@Entity
@DiscriminatorValue("ESSAY")
@NoArgsConstructor
public class EssayExam extends Exam {
    
    private static final long serialVersionUID = 1L;
    
    public EssayExam(String title, int sections, int questionsPerSection) {
        super(title, ExamType.ESSAY, sections, questionsPerSection);
    }
    
    /**
     * Essay exams require manual evaluation.
     * Demonstrates polymorphism - same method signature, different behavior.
     */
    @Override
    public int evaluate(String[] answers) {
        System.out.println("[MANUAL GRADING REQUIRED] Essay exam submitted for review.");
        System.out.println("Essays submitted: " + (answers != null ? answers.length : 0));
        
        // Basic word count check (demonstration only)
        if (answers != null) {
            for (int i = 0; i < answers.length; i++) {
                int wordCount = answers[i] != null ? answers[i].split("\\s+").length : 0;
                System.out.println("  Essay " + (i+1) + ": " + wordCount + " words");
            }
        }
        
        return 0; // Placeholder - requires manual grading
    }
    
    @Override
    public String toString() {
        return String.format("EssayExam[id=%d, title='%s', questions=%d, manualGading=true]",
                           getId(), getTitle(), getQuestions().size());
    }
}
