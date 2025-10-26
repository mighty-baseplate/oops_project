package com.examsystem;

import com.examsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Tests for Java Streams API usage.
 * Demonstrates filter, map, collect operations.
 */
class StreamsAPITest {
    
    private List<Student> students;
    
    @BeforeEach
    void setUp() {
        students = Arrays.asList(
            createStudent(1L, "Alice", 85),
            createStudent(2L, "Bob", 45),
            createStudent(3L, "Charlie", 75),
            createStudent(4L, "David", 30),
            createStudent(5L, "Eve", 95)
        );
    }
    
    private Student createStudent(Long id, String name, int score) {
        Student s = new Student(name, name.toLowerCase(), "pass");
        s.setId(id);
        s.addExamScore(1L, score);
        return s;
    }
    
    @Test
    @DisplayName("Test Streams - Filter Passed Students")
    void testFilterPassedStudents() {
        // When
        List<Student> passedStudents = students.stream()
                .filter(s -> s.getScoreForExam(1L) >= 50)
                .collect(Collectors.toList());
        
        // Then
        assertEquals(3, passedStudents.size(), "Should have 3 students with score >= 50");
        assertTrue(passedStudents.stream()
                .allMatch(s -> s.getScoreForExam(1L) >= 50));
    }
    
    @Test
    @DisplayName("Test Streams - Calculate Average Score")
    void testCalculateAverageScore() {
        // When
        double average = students.stream()
                .mapToInt(s -> s.getScoreForExam(1L))
                .average()
                .orElse(0.0);
        
        // Then
        assertEquals(66.0, average, "Average of 85,45,75,30,95 should be 66");
    }
    
    @Test
    @DisplayName("Test Streams - Map to Names")
    void testMapToNames() {
        // When
        List<String> names = students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        
        // Then
        assertEquals(5, names.size());
        assertTrue(names.contains("Alice"));
        assertTrue(names.contains("Bob"));
    }
    
    @Test
    @DisplayName("Test Streams - Count Operations")
    void testCountOperations() {
        // When
        long totalStudents = students.stream().count();
        long passedCount = students.stream()
                .filter(s -> s.getScoreForExam(1L) >= 50)
                .count();
        
        // Then
        assertEquals(5, totalStudents);
        assertEquals(3, passedCount);
    }
    
    @Test
    @DisplayName("Test Streams - Find Maximum Score")
    void testFindMaximumScore() {
        // When
        int maxScore = students.stream()
                .mapToInt(s -> s.getScoreForExam(1L))
                .max()
                .orElse(0);
        
        // Then
        assertEquals(95, maxScore, "Maximum score should be 95");
    }
    
    @Test
    @DisplayName("Test Streams - Find Minimum Score")
    void testFindMinimumScore() {
        // When
        int minScore = students.stream()
                .mapToInt(s -> s.getScoreForExam(1L))
                .min()
                .orElse(0);
        
        // Then
        assertEquals(30, minScore, "Minimum score should be 30");
    }
    
    @Test
    @DisplayName("Test Streams - Group By Pass/Fail")
    void testGroupByPassFail() {
        // When
        Map<Boolean, List<Student>> grouped = students.stream()
                .collect(Collectors.groupingBy(s -> s.getScoreForExam(1L) >= 50));
        
        // Then
        assertTrue(grouped.containsKey(true)); // Passed
        assertTrue(grouped.containsKey(false)); // Failed
        assertEquals(3, grouped.get(true).size());
        assertEquals(2, grouped.get(false).size());
    }
    
    @Test
    @DisplayName("Test Streams - Sorted by Score")
    void testSortedByScore() {
        // When
        List<Student> sorted = students.stream()
                .sorted((s1, s2) -> Integer.compare(
                    s2.getScoreForExam(1L), 
                    s1.getScoreForExam(1L)
                ))
                .collect(Collectors.toList());
        
        // Then
        assertEquals("Eve", sorted.get(0).getName()); // Highest: 95
        assertEquals("David", sorted.get(4).getName()); // Lowest: 30
    }
    
    @Test
    @DisplayName("Test Streams - Any Match")
    void testAnyMatch() {
        // When
        boolean hasHighScorer = students.stream()
                .anyMatch(s -> s.getScoreForExam(1L) > 90);
        
        // Then
        assertTrue(hasHighScorer, "Should have at least one student with score > 90");
    }
    
    @Test
    @DisplayName("Test Streams - All Match")
    void testAllMatch() {
        // When
        boolean allPassed = students.stream()
                .allMatch(s -> s.getScoreForExam(1L) >= 50);
        
        // Then
        assertFalse(allPassed, "Not all students have score >= 50");
    }
}
