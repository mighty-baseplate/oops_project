package com.examsystem.entity;

import com.examsystem.model.ExamType;
import com.examsystem.strategy.EvaluationStrategy;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract Exam entity demonstrating Abstraction and Inheritance.
 * Base class for different exam types (MCQ, Coding, Essay).
 * Uses Template Method pattern for evaluation.
 * Demonstrates: Abstract methods, Polymorphism, Reflection.
 */
@Entity
@Table(name = "exams")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "exam_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Exam implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Static counter for exam IDs (demonstrates static fields in OOP)
    private static int examCounter = 0;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType type;
    
    @Column
    private int totalMarks = 100;
    
    @Column
    private int sections = 1;
    
    @Column
    private int questionsPerSection = 10;
    
    @Column
    private int durationMinutes = 30;
    
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();
    
    // Thread-safe map for concurrent submissions (demonstrates concurrency)
    @Transient
    private Map<Long, Integer> studentScores = new ConcurrentHashMap<>();
    
    // Store student answers for manual grading
    @Transient
    private Map<Long, String[]> studentAnswers = new ConcurrentHashMap<>();
    
    // Strategy pattern - injected evaluation strategy
    @Transient
    private EvaluationStrategy evaluationStrategy;
    
    /**
     * Constructor increments static counter.
     */
    public Exam(String title, ExamType type, int sections, int questionsPerSection) {
        this.title = title;
        this.type = type;
        this.sections = sections;
        this.questionsPerSection = questionsPerSection;
        examCounter++;
    }
    
    /**
     * Abstract method - must be implemented by subclasses.
     * Demonstrates Abstraction principle.
     */
    public abstract int evaluate(String[] answers);
    
    /**
     * Template method using Strategy pattern.
     * Demonstrates polymorphic behavior.
     */
    public int evaluateWithStrategy(String[] answers, String[] correctAnswers) {
        if (evaluationStrategy != null) {
            return evaluationStrategy.evaluate(answers, correctAnswers);
        }
        return evaluate(answers);
    }
    
    /**
     * Synchronized method for concurrent exam submissions.
     * Demonstrates thread-safety in OOP.
     * Note: Actual database persistence happens in the service layer.
     */
    public synchronized int submit(Student student, String[] answers) {
        int score = evaluate(answers);
        studentScores.put(student.getId(), score);
        studentAnswers.put(student.getId(), answers); // Store answers in memory
        student.addExamScore(this.id, score);
        System.out.println("[CONCURRENT] Student " + student.getName() + 
                         " submitted exam. Score: " + score);
        return score;
    }
    
    /**
     * Get student's answers for this exam from memory.
     */
    public String[] getStudentAnswers(Long studentId) {
        return studentAnswers.getOrDefault(studentId, new String[0]);
    }
    
    /**
     * Add question to exam at specific section.
     */
    public void addQuestion(int section, Question question) {
        question.setSection(section);
        question.setExam(this);
        this.questions.add(question);
    }
    
    /**
     * Get questions for a specific section.
     */
    public List<Question> getQuestionsForSection(int section) {
        return questions.stream()
                .filter(q -> q.getSection() == section)
                .toList();
    }
    
    /**
     * Reflection method to print metadata.
     * Demonstrates Reflection API in Java.
     */
    public void printMetadata() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        EXAM METADATA (REFLECTION)          ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("  Class: " + getClass().getName());
        System.out.println("  Superclass: " + getClass().getSuperclass().getName());
        
        System.out.println("\n  Declared Fields:");
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.printf("    - %s %s%n", field.getType().getSimpleName(), field.getName());
        }
        
        System.out.println("\n  Declared Methods:");
        Method[] methods = getClass().getDeclaredMethods();
        for (Method method : methods) {
            System.out.printf("    - %s %s()%n", method.getReturnType().getSimpleName(), 
                            method.getName());
        }
        
        System.out.println("\n  Exam Details:");
        System.out.println("    Title: " + title);
        System.out.println("    Type: " + type);
        System.out.println("    Total Questions: " + questions.size());
        System.out.println("    Total Marks: " + totalMarks);
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
    
    /**
     * Get total exam counter (static method).
     */
    public static int getTotalExams() {
        return examCounter;
    }
    
    public void setEvaluationStrategy(EvaluationStrategy strategy) {
        this.evaluationStrategy = strategy;
    }
}
