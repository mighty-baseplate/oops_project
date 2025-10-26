package com.examsystem.config;

import com.examsystem.entity.*;
import com.examsystem.model.ExamType;
import com.examsystem.model.Role;
import com.examsystem.repository.ExamRepository;
import com.examsystem.repository.QuestionRepository;
import com.examsystem.repository.StudentRepository;
import com.examsystem.service.IExamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Data Initializer for Demo/Development.
 * Creates sample students, exams, and questions.
 */
@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initData(StudentRepository studentRepository,
                                     ExamRepository examRepository,
                                     QuestionRepository questionRepository,
                                     IExamService examService,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║        Initializing Demo Data...                      ║");
            System.out.println("╚════════════════════════════════════════════════════════╝\n");
            
            // Create Admin User
            Student admin = new Student();
            admin.setName("Administrator");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            studentRepository.save(admin);
            System.out.println("✓ Created Admin: admin/admin123");
            
            // Create Sample Students
            for (int i = 1; i <= 5; i++) {
                Student student = new Student();
                student.setName("Student " + i);
                student.setUsername("student" + i);
                student.setPassword(passwordEncoder.encode("pass123"));
                student.setRole(Role.STUDENT);
                studentRepository.save(student);
            }
            System.out.println("✓ Created 5 Students: student1-5/pass123");
            
            // Create MCQ Exam using Factory Pattern
            System.out.println("\n[FACTORY PATTERN] Creating MCQ Exam...");
            Exam mcqExam = examService.createExam(ExamType.MCQ, "Java OOP Fundamentals", 1, 10);
            mcqExam.setDurationMinutes(30);
            mcqExam.setTotalMarks(100);
            
            // Save exam first, then add questions
            mcqExam = examRepository.save(mcqExam);
            addMCQQuestions(mcqExam, questionRepository);
            System.out.println("✓ Created MCQ Exam with 10 questions");
            
            // Create Coding Exam using Factory Pattern
            System.out.println("\n[FACTORY PATTERN] Creating Coding Exam...");
            Exam codingExam = examService.createExam(ExamType.CODING, "Java Programming Challenge", 1, 3);
            codingExam.setDurationMinutes(60);
            codingExam.setTotalMarks(100);
            
            // Save exam first, then add questions
            codingExam = examRepository.save(codingExam);
            addCodingQuestions(codingExam, questionRepository);
            System.out.println("✓ Created Coding Exam with 3 questions");
            
            // Create Essay Exam using Factory Pattern
            System.out.println("\n[FACTORY PATTERN] Creating Essay Exam...");
            Exam essayExam = examService.createExam(ExamType.ESSAY, "Software Design Principles", 1, 3);
            essayExam.setDurationMinutes(45);
            essayExam.setTotalMarks(100);
            
            // Save exam first, then add questions
            essayExam = examRepository.save(essayExam);
            addEssayQuestions(essayExam, questionRepository);
            System.out.println("✓ Created Essay Exam with 3 questions");
            
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║        Demo Data Initialized Successfully!            ║");
            System.out.println("║                                                        ║");
            System.out.println("║  Login Credentials:                                    ║");
            System.out.println("║  Admin:   admin / admin123                             ║");
            System.out.println("║  Student: student1 / pass123                           ║");
            System.out.println("╚════════════════════════════════════════════════════════╝\n");
        };
    }
    
    private void addMCQQuestions(Exam exam, QuestionRepository questionRepository) {
        String[][] questions = {
            {"What is encapsulation in OOP?", 
             "Hiding implementation details", "Inheritance", "Polymorphism", "Abstraction",
             "Hiding implementation details"},
            
            {"Which keyword is used for inheritance in Java?",
             "extends", "implements", "inherit", "super",
             "extends"},
            
            {"What is polymorphism?",
             "Many forms", "Single form", "No form", "Abstract form",
             "Many forms"},
            
            {"Which is NOT a pillar of OOP?",
             "Compilation", "Encapsulation", "Inheritance", "Polymorphism",
             "Compilation"},
            
            {"What does 'private' access modifier do?",
             "Restricts access to class only", "Public access", "Protected access", "Default access",
             "Restricts access to class only"},
            
            {"Abstract class can be instantiated?",
             "False", "True", "Sometimes", "Depends",
             "False"},
            
            {"Interface can have implementation in Java 8+?",
             "True (default methods)", "False", "Never", "Only static",
             "True (default methods)"},
            
            {"Constructor name must match?",
             "Class name", "Method name", "Variable name", "Package name",
             "Class name"},
            
            {"super keyword is used for?",
             "Parent class reference", "This class", "Child class", "Interface",
             "Parent class reference"},
            
            {"Static members belong to?",
             "Class", "Object", "Method", "Package",
             "Class"}
        };
        
        for (int i = 0; i < questions.length; i++) {
            Question q = new Question();
            q.setText(questions[i][0]);
            q.setOptionsJson(String.format("[\"%s\",\"%s\",\"%s\",\"%s\"]", 
                questions[i][1], questions[i][2], questions[i][3], questions[i][4]));
            q.setCorrectAnswer(questions[i][5]);
            q.setSection(1);
            q.setExam(exam);
            questionRepository.save(q);
        }
    }
    
    private void addCodingQuestions(Exam exam, QuestionRepository questionRepository) {
        String[] questions = {
            "Implement a class 'BankAccount' with encapsulation (private balance, public deposit/withdraw methods).",
            "Create a class hierarchy: Animal -> Dog/Cat with method overriding for makeSound().",
            "Implement a Factory pattern to create different types of vehicles (Car, Bike, Truck)."
        };
        
        for (String qText : questions) {
            Question q = new Question();
            q.setText(qText);
            q.setCorrectAnswer("Code implementation required");
            q.setSection(1);
            q.setExam(exam);
            questionRepository.save(q);
        }
    }
    
    private void addEssayQuestions(Exam exam, QuestionRepository questionRepository) {
        String[] questions = {
            "Explain the SOLID principles with real-world examples.",
            "Discuss the difference between Abstract Classes and Interfaces in Java.",
            "Describe how Design Patterns improve code maintainability and scalability."
        };
        
        for (String qText : questions) {
            Question q = new Question();
            q.setText(qText);
            q.setCorrectAnswer("Essay answer required");
            q.setSection(1);
            q.setExam(exam);
            questionRepository.save(q);
        }
    }
}
