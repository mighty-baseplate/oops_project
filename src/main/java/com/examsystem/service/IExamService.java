package com.examsystem.service;

import com.examsystem.entity.Exam;
import com.examsystem.entity.Question;
import com.examsystem.entity.Student;
import com.examsystem.model.ExamType;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Exam operations.
 * Demonstrates SOLID principles - Interface Segregation.
 * Allows different implementations (mock, real, etc.).
 */
public interface IExamService {
    
    /**
     * Create a new exam using Factory pattern.
     */
    Exam createExam(ExamType type, String title, int sections, int questionsPerSection);
    
    /**
     * Save exam to database.
     */
    Exam saveExam(Exam exam);
    
    /**
     * Get exam by ID.
     */
    Exam getExamById(Long id);
    
    /**
     * Get all exams.
     */
    List<Exam> getAllExams();
    
    /**
     * Get exams by type.
     */
    List<Exam> getExamsByType(ExamType type);
    
    /**
     * Add question to exam.
     */
    void addQuestionToExam(Long examId, int section, Question question);
    
    /**
     * Submit exam answers (thread-safe).
     */
    int submitExam(Long examId, Student student, String[] answers);
    
    /**
     * Get students who took specific exam.
     */
    List<Student> getStudentsForExam(Long examId);
    
    /**
     * Get students who passed exam (score >= passMark).
     * Demonstrates Streams API.
     */
    List<Student> getPassedStudents(Long examId, int passMark);
    
    /**
     * Get average score for exam.
     * Demonstrates Streams API.
     */
    double getAverageScore(Long examId);
    
    /**
     * Group students by exam type.
     * Demonstrates Streams groupingBy.
     */
    Map<String, List<Student>> groupStudentsByExamType(List<Student> students);
    
    /**
     * Save exam results to CSV file.
     */
    void saveResultsToFile(String filePath, Map<Student, Integer> results);
}
