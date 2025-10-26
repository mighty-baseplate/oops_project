package com.examsystem.model;

/**
 * Enumeration for different types of exams.
 * Demonstrates type-safe constants in OOP.
 */
public enum ExamType {
    MCQ("Multiple Choice Questions", true),
    CODING("Coding Challenge", false),
    ESSAY("Essay Type", false);
    
    private final String displayName;
    private final boolean autoGradable;
    
    ExamType(String displayName, boolean autoGradable) {
        this.displayName = displayName;
        this.autoGradable = autoGradable;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isAutoGradable() {
        return autoGradable;
    }
}
