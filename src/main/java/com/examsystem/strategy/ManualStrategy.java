package com.examsystem.strategy;

import org.springframework.stereotype.Component;

/**
 * Manual Evaluation Strategy for coding/essay exams.
 * Demonstrates Strategy Pattern - different evaluation approach.
 */
@Component
public class ManualStrategy implements EvaluationStrategy {
    
    /**
     * Returns 0 as placeholder - requires admin manual grading.
     * In production, this would queue the exam for admin review.
     */
    @Override
    public int evaluate(String[] answers, String[] correctAnswers) {
        System.out.println("[MANUAL STRATEGY] Exam requires manual grading.");
        System.out.println("Submitted answers: " + (answers != null ? answers.length : 0));
        
        // Log for admin review queue
        logForReview(answers);
        
        return 0; // Placeholder - admin will grade
    }
    
    @Override
    public String getStrategyName() {
        return "Manual Grading Strategy";
    }
    
    /**
     * Logs answers for admin review (demonstration).
     */
    private void logForReview(String[] answers) {
        if (answers != null) {
            System.out.println("Logging " + answers.length + " answers for manual review...");
        }
    }
}
