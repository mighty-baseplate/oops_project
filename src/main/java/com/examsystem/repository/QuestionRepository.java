package com.examsystem.repository;

import com.examsystem.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * JPA Repository for Question entity.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * Find questions by exam ID.
     */
    List<Question> findByExamId(Long examId);
    
    /**
     * Find questions by exam ID and section.
     */
    List<Question> findByExamIdAndSection(Long examId, int section);
}
