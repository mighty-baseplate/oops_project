package com.examsystem.exception;

/**
 * Custom exception for invalid exam answers.
 * Demonstrates exception handling in OOP.
 */
public class InvalidAnswerException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidAnswerException(String message) {
        super(message);
    }
    
    public InvalidAnswerException(String message, Throwable cause) {
        super(message, cause);
    }
}
