package com.examsystem.service;

import com.examsystem.entity.Exam;
import com.examsystem.entity.ExamSubmission;
import com.examsystem.entity.Question;
import com.examsystem.entity.Student;
import com.examsystem.exception.ExamNotFoundException;
import com.examsystem.factory.ExamFactory;
import com.examsystem.model.ExamType;
import com.examsystem.repository.ExamRepository;
import com.examsystem.repository.ExamSubmissionRepository;
import com.examsystem.repository.QuestionRepository;
import com.examsystem.utils.FileIOUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Exam Service Implementation.
 * Demonstrates SOLID principles:
 * - Single Responsibility: Handles exam-related business logic
 * - Dependency Inversion: Depends on abstractions (interfaces)
 * - Interface Segregation: Implements specific interface
 */
@Service
@Transactional
public class ExamServiceImpl implements IExamService {
    
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamSubmissionRepository submissionRepository;
    private final ExamFactory examFactory;
    
    // In-memory storage for exam-student mapping (demonstrates Collections)
    private static final Map<Long, List<Student>> examStudentsMap = new ConcurrentHashMap<>();
    
    public ExamServiceImpl(ExamRepository examRepository,
                          QuestionRepository questionRepository,
                          ExamSubmissionRepository submissionRepository,
                          ExamFactory examFactory) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.submissionRepository = submissionRepository;
        this.examFactory = examFactory;
    }
    
    @Override
    public Exam createExam(ExamType type, String title, int sections, int questionsPerSection) {
        // Uses Factory pattern
        return examFactory.createExam(type, title, sections, questionsPerSection);
    }
    
    @Override
    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }
    
    @Override
    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException(id));
    }
    
    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }
    
    @Override
    public List<Exam> getExamsByType(ExamType type) {
        return examRepository.findByType(type);
    }
    
    @Override
    public void addQuestionToExam(Long examId, int section, Question question) {
        Exam exam = getExamById(examId);
        question.setExam(exam);
        question.setSection(section);
        questionRepository.save(question);
        exam.getQuestions().add(question);
        examRepository.save(exam);
    }
    
    /**
     * Thread-safe exam submission using synchronized method.
     * Demonstrates concurrency handling.
     * Now persists submissions to database.
     */
    @Override
    public synchronized int submitExam(Long examId, Student student, String[] answers) {
        Exam exam = getExamById(examId);
        
        // Use synchronized method from Exam entity
        int score = exam.submit(student, answers);
        
        // Save submission to database
        ExamSubmission submission = ExamSubmission.builder()
                .exam(exam)
                .student(student)
                .score(score)
                .graded(exam.getType() == ExamType.MCQ) // Auto-graded for MCQ
                .build();
        submission.setAnswers(answers);
        submissionRepository.save(submission);
        
        // Add to exam-students map
        examStudentsMap.computeIfAbsent(examId, k -> new ArrayList<>()).add(student);
        
        return student.getScoreForExam(examId);
    }
    
    @Override
    public List<Student> getStudentsForExam(Long examId) {
        return examStudentsMap.getOrDefault(examId, new ArrayList<>());
    }
    
    /**
     * Demonstrates Streams API - filter operation.
     */
    @Override
    public List<Student> getPassedStudents(Long examId, int passMark) {
        return getStudentsForExam(examId).stream()
                .filter(student -> student.getScoreForExam(examId) >= passMark)
                .collect(Collectors.toList());
    }
    
    /**
     * Demonstrates Streams API - mapToInt and average.
     */
    @Override
    public double getAverageScore(Long examId) {
        List<Student> students = getStudentsForExam(examId);
        if (students.isEmpty()) return 0.0;
        
        return students.stream()
                .mapToInt(student -> student.getScoreForExam(examId))
                .average()
                .orElse(0.0);
    }
    
    /**
     * Demonstrates Streams API - groupingBy collector.
     */
    @Override
    public Map<String, List<Student>> groupStudentsByExamType(List<Student> students) {
        return students.stream()
                .filter(s -> s.getCurrentExamId() != null)
                .collect(Collectors.groupingBy(student -> {
                    try {
                        Exam exam = getExamById(student.getCurrentExamId());
                        return exam.getType().getDisplayName();
                    } catch (Exception e) {
                        return "Unknown";
                    }
                }));
    }
    
    @Override
    public void saveResultsToFile(String filePath, Map<Student, Integer> results) {
        try {
            FileIOUtil.saveResultsToCSV(filePath, results);
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
        }
    }
    
    /**
     * Get all exam statistics (for admin dashboard).
     */
    public Map<String, Object> getExamStatistics(Long examId) {
        Map<String, Object> stats = new HashMap<>();
        List<Student> students = getStudentsForExam(examId);
        
        stats.put("totalStudents", students.size());
        stats.put("averageScore", getAverageScore(examId));
        stats.put("passedStudents", getPassedStudents(examId, 50).size());
        stats.put("passPercentage", students.isEmpty() ? 0 : 
                  (getPassedStudents(examId, 50).size() * 100.0) / students.size());
        
        return stats;
    }
}
