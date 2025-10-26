package com.examsystem.entity;

import com.examsystem.exception.InvalidAnswerException;
import com.examsystem.model.ExamType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * MCQ Exam subclass demonstrating Inheritance and Polymorphism.
 * Implements auto-grading logic by overriding abstract evaluate() method.
 */
@Entity
@DiscriminatorValue("MCQ")
@NoArgsConstructor
public class MCQExam extends Exam {
    
    private static final long serialVersionUID = 1L;
    
    public MCQExam(String title, int sections, int questionsPerSection) {
        super(title, ExamType.MCQ, sections, questionsPerSection);
    }
    
    /**
     * Override abstract method to implement MCQ-specific evaluation.
     * Demonstrates Polymorphism - same method, different implementation.
     * 
     * @param answers Student's submitted answers
     * @return Score out of total marks
     * @throws InvalidAnswerException if answer format is invalid
     */
    @Override
    public int evaluate(String[] answers) {
        if (answers == null || answers.length == 0) {
            throw new InvalidAnswerException("Answers cannot be null or empty");
        }
        
        if (answers.length != getQuestions().size()) {
            throw new InvalidAnswerException(
                String.format("Expected %d answers, got %d", 
                            getQuestions().size(), answers.length)
            );
        }
        
        int correctCount = 0;
        for (int i = 0; i < answers.length && i < getQuestions().size(); i++) {
            Question q = getQuestions().get(i);
            if (q.getCorrectAnswer().equalsIgnoreCase(answers[i].trim())) {
                correctCount++;
            }
        }
        
        // Calculate percentage score
        int totalQuestions = getQuestions().size();
        return totalQuestions > 0 ? (correctCount * getTotalMarks()) / totalQuestions : 0;
    }
    
    /**
     * Overriding toString() for better object representation.
     */
    @Override
    public String toString() {
        return String.format("MCQExam[id=%d, title='%s', questions=%d, marks=%d]",
                           getId(), getTitle(), getQuestions().size(), getTotalMarks());
    }
}
