package com.examsystem.strategy;

/**
 * Strategy Pattern Interface for exam evaluation.
 * Demonstrates Strategy Design Pattern and Interface-based programming.
 * Allows different evaluation algorithms to be injected at runtime.
 */
public interface EvaluationStrategy {
    
    /**
     * Evaluate student answers against correct answers.
     * 
     * @param answers Student's submitted answers
     * @param correctAnswers Correct answer key
     * @return Score obtained
     */
    int evaluate(String[] answers, String[] correctAnswers);
    
    /**
     * Get strategy name for logging/debugging.
     */
    String getStrategyName();
}
