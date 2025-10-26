package com.examsystem;

import com.examsystem.exception.InvalidAnswerException;
import com.examsystem.strategy.MCQStrategy;
import com.examsystem.strategy.ManualStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Tests for Strategy Pattern.
 * Tests different evaluation strategies.
 */
class StrategyPatternTest {
    
    private MCQStrategy mcqStrategy;
    private ManualStrategy manualStrategy;
    
    @BeforeEach
    void setUp() {
        mcqStrategy = new MCQStrategy();
        manualStrategy = new ManualStrategy();
    }
    
    @Test
    @DisplayName("Test MCQ Strategy - All Correct")
    void testMCQStrategy_AllCorrect() {
        // Given
        String[] answers = {"A", "B", "C", "D", "A"};
        String[] correctAnswers = {"A", "B", "C", "D", "A"};
        
        // When
        int score = mcqStrategy.evaluate(answers, correctAnswers);
        
        // Then
        assertEquals(100, score, "All correct answers should give 100 marks");
    }
    
    @Test
    @DisplayName("Test MCQ Strategy - Half Correct")
    void testMCQStrategy_HalfCorrect() {
        // Given
        String[] answers = {"A", "B", "C", "D"};
        String[] correctAnswers = {"A", "X", "C", "Y"};
        
        // When
        int score = mcqStrategy.evaluate(answers, correctAnswers);
        
        // Then
        assertEquals(50, score, "50% correct should give 50 marks");
    }
    
    @Test
    @DisplayName("Test MCQ Strategy - None Correct")
    void testMCQStrategy_NoneCorrect() {
        // Given
        String[] answers = {"X", "Y", "Z"};
        String[] correctAnswers = {"A", "B", "C"};
        
        // When
        int score = mcqStrategy.evaluate(answers, correctAnswers);
        
        // Then
        assertEquals(0, score, "No correct answers should give 0 marks");
    }
    
    @Test
    @DisplayName("Test MCQ Strategy - Case Insensitive")
    void testMCQStrategy_CaseInsensitive() {
        // Given
        String[] answers = {"a", "B", "c"};
        String[] correctAnswers = {"A", "b", "C"};
        
        // When
        int score = mcqStrategy.evaluate(answers, correctAnswers);
        
        // Then
        assertEquals(100, score, "Case should not matter in MCQ grading");
    }
    
    @Test
    @DisplayName("Test MCQ Strategy - Invalid Answer Count")
    void testMCQStrategy_InvalidAnswerCount() {
        // Given
        String[] answers = {"A", "B"};
        String[] correctAnswers = {"A", "B", "C", "D"};
        
        // When & Then
        assertThrows(InvalidAnswerException.class, () -> {
            mcqStrategy.evaluate(answers, correctAnswers);
        }, "Should throw exception for mismatched answer count");
    }
    
    @Test
    @DisplayName("Test MCQ Strategy - Null Answers")
    void testMCQStrategy_NullAnswers() {
        // Given
        String[] correctAnswers = {"A", "B", "C"};
        
        // When & Then
        assertThrows(InvalidAnswerException.class, () -> {
            mcqStrategy.evaluate(null, correctAnswers);
        }, "Should throw exception for null answers");
    }
    
    @Test
    @DisplayName("Test Manual Strategy - Returns Zero")
    void testManualStrategy_ReturnsZero() {
        // Given
        String[] answers = {"Code here...", "More code..."};
        String[] correctAnswers = {"Expected code..."};
        
        // When
        int score = manualStrategy.evaluate(answers, correctAnswers);
        
        // Then
        assertEquals(0, score, "Manual strategy should always return 0");
    }
    
    @Test
    @DisplayName("Test Strategy Name")
    void testStrategyNames() {
        assertEquals("MCQ Auto-Grading Strategy", mcqStrategy.getStrategyName());
        assertEquals("Manual Grading Strategy", manualStrategy.getStrategyName());
    }
}
