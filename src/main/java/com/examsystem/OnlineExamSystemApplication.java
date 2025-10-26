package com.examsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for Secure Online Examination System.
 * Demonstrates OOP principles: Encapsulation, Inheritance, Polymorphism, Abstraction.
 * Design Patterns: Factory, Strategy.
 * Features: JWT Auth, Concurrent Exam Submissions, Streams, Serialization.
 * 
 * @author OOP Project Team
 * @version 1.0
 */
@SpringBootApplication
public class OnlineExamSystemApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OnlineExamSystemApplication.class, args);
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  Secure Online Examination System Started Successfully!  ║");
        System.out.println("║  Access: http://localhost:8080                            ║");
        System.out.println("║  H2 Console: http://localhost:8080/h2-console             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}
