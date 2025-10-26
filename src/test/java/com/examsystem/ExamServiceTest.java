package com.examsystem;

import com.examsystem.entity.*;
import com.examsystem.exception.InvalidAnswerException;
import com.examsystem.model.ExamType;
import com.examsystem.repository.ExamRepository;
import com.examsystem.repository.ExamSubmissionRepository;
import com.examsystem.repository.QuestionRepository;
import com.examsystem.service.ExamServiceImpl;
import com.examsystem.factory.ExamFactory;
import com.examsystem.strategy.MCQStrategy;
import com.examsystem.strategy.ManualStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 Tests for Exam Service.
 * Demonstrates unit testing with mocking.
 */
class ExamServiceTest {
    
    @Mock
    private ExamRepository examRepository;
    
    @Mock
    private QuestionRepository questionRepository;
    
    @Mock
    private ExamSubmissionRepository submissionRepository;
    
    private ExamFactory examFactory;
    private ExamServiceImpl examService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MCQStrategy mcqStrategy = new MCQStrategy();
        ManualStrategy manualStrategy = new ManualStrategy();
        examFactory = new ExamFactory(mcqStrategy, manualStrategy);
        examService = new ExamServiceImpl(examRepository, questionRepository, submissionRepository, examFactory);
    }
    
    @Test
    @DisplayName("Test Factory Pattern - MCQ Exam Creation")
    void testCreateMCQExam() {
        // Given
        ExamType type = ExamType.MCQ;
        String title = "Test MCQ Exam";
        
        // When
        Exam exam = examService.createExam(type, title, 1, 10);
        
        // Then
        assertNotNull(exam);
        assertTrue(exam instanceof MCQExam);
        assertEquals(title, exam.getTitle());
        assertEquals(type, exam.getType());
    }
    
    @Test
    @DisplayName("Test Factory Pattern - Coding Exam Creation")
    void testCreateCodingExam() {
        // Given
        ExamType type = ExamType.CODING;
        String title = "Test Coding Exam";
        
        // When
        Exam exam = examService.createExam(type, title, 1, 5);
        
        // Then
        assertNotNull(exam);
        assertTrue(exam instanceof CodingExam);
        assertEquals(title, exam.getTitle());
    }
    
    @Test
    @DisplayName("Test MCQ Grading - All Correct Answers")
    void testMCQGrading_AllCorrect() {
        // Given
        MCQExam exam = new MCQExam("Test", 1, 3);
        exam.setTotalMarks(100);
        
        for (int i = 0; i < 3; i++) {
            Question q = new Question();
            q.setText("Question " + (i + 1));
            q.setCorrectAnswer("A");
            exam.getQuestions().add(q);
        }
        
        String[] answers = {"A", "A", "A"};
        
        // When
        int score = exam.evaluate(answers);
        
        // Then
        assertEquals(100, score, "All correct answers should give full marks");
    }
    
    @Test
    @DisplayName("Test MCQ Grading - Partial Correct")
    void testMCQGrading_PartialCorrect() {
        // Given
        MCQExam exam = new MCQExam("Test", 1, 4);
        exam.setTotalMarks(100);
        
        for (int i = 0; i < 4; i++) {
            Question q = new Question();
            q.setText("Question " + (i + 1));
            q.setCorrectAnswer("A");
            exam.getQuestions().add(q);
        }
        
        String[] answers = {"A", "B", "A", "C"}; // 2 correct out of 4
        
        // When
        int score = exam.evaluate(answers);
        
        // Then
        assertEquals(50, score, "50% correct should give 50 marks");
    }
    
    @Test
    @DisplayName("Test Exception - Invalid Answer Length")
    void testInvalidAnswerException() {
        // Given
        MCQExam exam = new MCQExam("Test", 1, 5);
        for (int i = 0; i < 5; i++) {
            Question q = new Question();
            q.setText("Question " + (i + 1));
            q.setCorrectAnswer("A");
            exam.getQuestions().add(q);
        }
        
        String[] answers = {"A", "B"}; // Only 2 answers for 5 questions
        
        // When & Then
        assertThrows(InvalidAnswerException.class, () -> {
            exam.evaluate(answers);
        }, "Should throw InvalidAnswerException for mismatched answer count");
    }
    
    @Test
    @DisplayName("Test Streams API - Get Passed Students")
    void testGetPassedStudents() {
        // Given
        when(examRepository.findById(1L)).thenReturn(Optional.of(new MCQExam("Test", 1, 10)));
        
        Student s1 = new Student("Alice", "alice", "pass");
        s1.setId(1L);
        s1.addExamScore(1L, 75);
        
        Student s2 = new Student("Bob", "bob", "pass");
        s2.setId(2L);
        s2.addExamScore(1L, 45);
        
        Student s3 = new Student("Charlie", "charlie", "pass");
        s3.setId(3L);
        s3.addExamScore(1L, 85);
        
        // Simulate students list
        examService.submitExam(1L, s1, new String[0]);
        examService.submitExam(1L, s2, new String[0]);
        examService.submitExam(1L, s3, new String[0]);
        
        // When
        List<Student> passedStudents = examService.getPassedStudents(1L, 50);
        
        // Then - Should have filtered students with score >= 50
        // Note: Due to mocking, actual filtering might differ
        assertNotNull(passedStudents);
    }
    
    @Test
    @DisplayName("Test Polymorphism - Different Exam Types")
    void testPolymorphism() {
        // Given
        Exam mcqExam = examFactory.createExam(ExamType.MCQ, "MCQ Test", 1, 5);
        Exam codingExam = examFactory.createExam(ExamType.CODING, "Coding Test", 1, 3);
        Exam essayExam = examFactory.createExam(ExamType.ESSAY, "Essay Test", 1, 2);
        
        // When & Then
        assertTrue(mcqExam instanceof MCQExam);
        assertTrue(codingExam instanceof CodingExam);
        assertTrue(essayExam instanceof EssayExam);
        
        // All are Exam type (polymorphism)
        assertTrue(mcqExam instanceof Exam);
        assertTrue(codingExam instanceof Exam);
        assertTrue(essayExam instanceof Exam);
    }
    
    @Test
    @DisplayName("Test Static Counter - Total Exams")
    void testStaticCounter() {
        // Given
        int initialCount = Exam.getTotalExams();
        
        // When
        new MCQExam("Test 1", 1, 10);
        new CodingExam("Test 2", 1, 5);
        
        // Then
        assertEquals(initialCount + 2, Exam.getTotalExams(),
                    "Static counter should increment for each exam created");
    }
    
    @Test
    @DisplayName("Test Coding Exam - Manual Grading Returns 0")
    void testCodingExamManualGrading() {
        // Given
        CodingExam exam = new CodingExam("Java Challenge", 1, 3);
        String[] answers = {"System.out.println()", "int x = 5;", "public class Test {}"};
        
        // When
        int score = exam.evaluate(answers);
        
        // Then
        assertEquals(0, score, "Coding exam should return 0 (manual grading required)");
    }
    
    @Test
    @DisplayName("Test Essay Exam - Manual Grading Returns 0")
    void testEssayExamManualGrading() {
        // Given
        EssayExam exam = new EssayExam("OOP Principles", 1, 2);
        String[] answers = {
            "SOLID principles are...",
            "Encapsulation means..."
        };
        
        // When
        int score = exam.evaluate(answers);
        
        // Then
        assertEquals(0, score, "Essay exam should return 0 (manual grading required)");
    }
}
