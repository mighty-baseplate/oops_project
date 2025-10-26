package com.examsystem.exception;

/**
 * Exception thrown when exam is not found.
 */
public class ExamNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public ExamNotFoundException(String message) {
        super(message);
    }
    
    public ExamNotFoundException(Long examId) {
        super("Exam not found with id: " + examId);
    }
}
