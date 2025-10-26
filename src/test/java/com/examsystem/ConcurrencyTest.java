package com.examsystem;

import com.examsystem.entity.MCQExam;
import com.examsystem.entity.Question;
import com.examsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Tests for Concurrency.
 * Tests synchronized exam submission with multiple threads.
 */
class ConcurrencyTest {
    
    private MCQExam exam;
    
    @BeforeEach
    void setUp() {
        exam = new MCQExam("Concurrent Test", 1, 5);
        exam.setTotalMarks(100);
        
        // Add questions
        for (int i = 0; i < 5; i++) {
            Question q = new Question();
            q.setText("Question " + (i + 1));
            q.setCorrectAnswer("A");
            exam.getQuestions().add(q);
        }
    }
    
    @Test
    @DisplayName("Test Concurrent Exam Submission")
    void testConcurrentSubmission() throws InterruptedException {
        // Given
        int numStudents = 10;
        CountDownLatch latch = new CountDownLatch(numStudents);
        List<Student> students = new ArrayList<>();
        
        for (int i = 0; i < numStudents; i++) {
            Student s = new Student("Student" + i, "user" + i, "pass");
            s.setId((long) i);
            students.add(s);
        }
        
        // When - Submit exams concurrently
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for (Student student : students) {
            executor.submit(() -> {
                try {
                    String[] answers = {"A", "A", "A", "A", "A"}; // All correct
                    exam.submit(student, answers);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        
        // Then
        assertEquals(numStudents, exam.getStudentScores().size(),
                    "All students should have submitted");
        
        // Verify all got correct score (100)
        exam.getStudentScores().values().forEach(score -> {
            assertEquals(100, score, "All correct answers should give 100");
        });
    }
    
    @Test
    @DisplayName("Test Thread Safety with Mixed Results")
    void testThreadSafetyMixedResults() throws InterruptedException, ExecutionException {
        // Given
        int numThreads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();
        
        // When - Submit with different answer patterns
        for (int i = 0; i < numThreads; i++) {
            final int studentId = i;
            Future<Integer> future = executor.submit(() -> {
                Student s = new Student("Student" + studentId, "user" + studentId, "pass");
                s.setId((long) studentId);
                
                // Different answer patterns
                String[] answers = studentId % 2 == 0 
                    ? new String[]{"A", "A", "A", "A", "A"} // All correct
                    : new String[]{"B", "B", "B", "B", "B"}; // All wrong
                
                exam.submit(s, answers);
                return s.getScoreForExam(exam.getId());
            });
            futures.add(future);
        }
        
        // Then
        for (int i = 0; i < futures.size(); i++) {
            int score = futures.get(i).get();
            if (i % 2 == 0) {
                assertEquals(100, score, "Even indexed students should get 100");
            } else {
                assertEquals(0, score, "Odd indexed students should get 0");
            }
        }
        
        executor.shutdown();
    }
    
    @Test
    @DisplayName("Test Synchronized Method Prevents Race Conditions")
    void testSynchronizedMethod() throws InterruptedException {
        // Given
        int numThreads = 100;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numThreads);
        
        // When - All threads try to submit at the same time
        for (int i = 0; i < numThreads; i++) {
            final int studentId = i;
            new Thread(() -> {
                try {
                    startLatch.await(); // Wait for signal
                    Student s = new Student("Student" + studentId, "user" + studentId, "pass");
                    s.setId((long) studentId);
                    exam.submit(s, new String[]{"A", "A", "A", "A", "A"});
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        
        startLatch.countDown(); // Signal all threads to start
        endLatch.await(10, TimeUnit.SECONDS);
        
        // Then
        assertEquals(numThreads, exam.getStudentScores().size(),
                    "Synchronized method should handle all submissions correctly");
    }
}
