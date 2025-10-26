package com.examsystem.strategy;

import com.examsystem.exception.InvalidAnswerException;
import org.springframework.stereotype.Component;

/**
 * MCQ Evaluation Strategy implementation.
 * Demonstrates Strategy Pattern - concrete strategy for MCQ grading.
 */
@Component
public class MCQStrategy implements EvaluationStrategy {
    
    private static final int TOTAL_MARKS = 100;
    
    /**
     * Evaluates MCQ answers by comparing with correct answers.
     * Each correct answer gets equal weightage.
     */
    @Override
    public int evaluate(String[] answers, String[] correctAnswers) {
        if (answers == null || correctAnswers == null) {
            throw new InvalidAnswerException("Answers or correct answers cannot be null");
        }
        
        if (answers.length != correctAnswers.length) {
            throw new InvalidAnswerException(
                String.format("Answer count mismatch: expected %d, got %d",
                            correctAnswers.length, answers.length)
            );
        }
        
        int correctCount = 0;
        for (int i = 0; i < answers.length; i++) {
            if (answers[i] != null && 
                answers[i].trim().equalsIgnoreCase(correctAnswers[i].trim())) {
                correctCount++;
            }
        }
        
        // Calculate percentage score
        return correctAnswers.length > 0 ? 
               (correctCount * TOTAL_MARKS) / correctAnswers.length : 0;
    }
    
    @Override
    public String getStrategyName() {
        return "MCQ Auto-Grading Strategy";
    }
}
