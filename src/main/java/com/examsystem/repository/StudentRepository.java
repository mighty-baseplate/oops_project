package com.examsystem.repository;

import com.examsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * JPA Repository for Student entity.
 * Demonstrates Spring Data JPA and Repository pattern.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find student by username.
     * Used for authentication.
     */
    Optional<Student> findByUsername(String username);
    
    /**
     * Check if username exists.
     */
    boolean existsByUsername(String username);
}
