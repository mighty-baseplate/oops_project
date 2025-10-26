package com.examsystem.repository;

import com.examsystem.entity.Exam;
import com.examsystem.model.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * JPA Repository for Exam entity.
 * Demonstrates Spring Data JPA custom queries.
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    /**
     * Find exams by type.
     */
    List<Exam> findByType(ExamType type);
    
    /**
     * Find exams by title containing keyword.
     */
    List<Exam> findByTitleContainingIgnoreCase(String keyword);
}
