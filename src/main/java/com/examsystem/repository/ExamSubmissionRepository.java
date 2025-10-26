package com.examsystem.repository;

import com.examsystem.entity.ExamSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, Long> {
    
    /**
     * Find all submissions for a specific exam.
     */
    List<ExamSubmission> findByExamId(Long examId);
    
    /**
     * Find submission by exam and student.
     */
    Optional<ExamSubmission> findByExamIdAndStudentId(Long examId, Long studentId);
    
    /**
     * Find all submissions by a student.
     */
    List<ExamSubmission> findByStudentId(Long studentId);
    
    /**
     * Find ungraded submissions for an exam.
     */
    List<ExamSubmission> findByExamIdAndGradedFalse(Long examId);
}
