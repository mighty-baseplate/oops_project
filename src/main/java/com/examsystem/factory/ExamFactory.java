package com.examsystem.factory;

import com.examsystem.entity.*;
import com.examsystem.model.ExamType;
import com.examsystem.strategy.EvaluationStrategy;
import com.examsystem.strategy.MCQStrategy;
import com.examsystem.strategy.ManualStrategy;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern for creating different types of exams.
 * Demonstrates Factory Design Pattern and Polymorphism.
 * Centralizes object creation logic.
 */
@Component
public class ExamFactory {
    
    private final MCQStrategy mcqStrategy;
    private final ManualStrategy manualStrategy;
    
    public ExamFactory(MCQStrategy mcqStrategy, ManualStrategy manualStrategy) {
        this.mcqStrategy = mcqStrategy;
        this.manualStrategy = manualStrategy;
    }
    
    /**
     * Factory method to create exam based on type.
     * Returns appropriate subclass - demonstrates Polymorphism.
     * 
     * @param type ExamType enum
     * @param title Exam title
     * @param sections Number of sections
     * @param questionsPerSection Questions per section
     * @return Exam instance (MCQExam, CodingExam, or EssayExam)
     */
    public Exam createExam(ExamType type, String title, int sections, int questionsPerSection) {
        Exam exam;
        
        switch (type) {
            case MCQ:
                exam = new MCQExam(title, sections, questionsPerSection);
                exam.setEvaluationStrategy(mcqStrategy);
                System.out.println("[FACTORY] Created MCQExam with auto-grading strategy");
                break;
                
            case CODING:
                exam = new CodingExam(title, sections, questionsPerSection);
                exam.setEvaluationStrategy(manualStrategy);
                System.out.println("[FACTORY] Created CodingExam with manual grading strategy");
                break;
                
            case ESSAY:
                exam = new EssayExam(title, sections, questionsPerSection);
                exam.setEvaluationStrategy(manualStrategy);
                System.out.println("[FACTORY] Created EssayExam with manual grading strategy");
                break;
                
            default:
                throw new IllegalArgumentException("Unknown exam type: " + type);
        }
        
        return exam;
    }
    
    /**
     * Overloaded factory method with default values.
     * Demonstrates method overloading in OOP.
     */
    public Exam createExam(ExamType type, String title) {
        return createExam(type, title, 1, 10);
    }
    
    /**
     * Creates exam from string type (for REST API convenience).
     */
    public Exam createExam(String typeStr, String title, int sections, int questionsPerSection) {
        try {
            ExamType type = ExamType.valueOf(typeStr.toUpperCase());
            return createExam(type, title, sections, questionsPerSection);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid exam type: " + typeStr + 
                                             ". Valid types: MCQ, CODING, ESSAY");
        }
    }
}
