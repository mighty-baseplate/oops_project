package com.examsystem.controller;

import com.examsystem.entity.Exam;
import com.examsystem.entity.ExamSubmission;
import com.examsystem.entity.Question;
import com.examsystem.entity.Student;
import com.examsystem.model.ExamSubmissionRequest;
import com.examsystem.repository.ExamSubmissionRepository;
import com.examsystem.service.IExamService;
import com.examsystem.service.IStudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student Controller.
 * Handles student dashboard, exam taking, and submission.
 * Demonstrates @Controller, @GetMapping, @PostMapping annotations.
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    
    private final IExamService examService;
    private final IStudentService studentService;
    private final ExamSubmissionRepository submissionRepository;
    
    public StudentController(IExamService examService, IStudentService studentService, 
                           ExamSubmissionRepository submissionRepository) {
        this.examService = examService;
        this.studentService = studentService;
        this.submissionRepository = submissionRepository;
    }
    
    /**
     * Student dashboard - shows available exams and previous attempts.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Student student = studentService.findByUsername(auth.getName());
        List<Exam> exams = examService.getAllExams();
        
        // Get all previous submissions for this student
        List<ExamSubmission> previousSubmissions = submissionRepository.findByStudentId(student.getId());
        
        model.addAttribute("student", student);
        model.addAttribute("exams", exams);
        model.addAttribute("totalExams", exams.size());
        model.addAttribute("previousSubmissions", previousSubmissions);
        model.addAttribute("totalAttempts", previousSubmissions.size());
        
        return "student/dashboard";
    }
    
    /**
     * Start exam - shows exam questions.
     */
    @GetMapping("/exam/{examId}")
    public String startExam(@PathVariable Long examId, Model model, Authentication auth) {
        Student student = studentService.findByUsername(auth.getName());
        Exam exam = examService.getExamById(examId);
        
        model.addAttribute("student", student);
        model.addAttribute("exam", exam);
        model.addAttribute("questions", exam.getQuestions());
        
        return "student/exam";
    }
    
    /**
     * Submit exam - processes answers and shows results.
     * Demonstrates synchronized submission (concurrency).
     */
    @PostMapping("/submit")
    public String submitExam(@RequestParam Long examId,
                            @RequestParam String[] answers,
                            Authentication auth,
                            Model model) {
        Student student = studentService.findByUsername(auth.getName());
        
        // Thread-safe submission
        int score = examService.submitExam(examId, student, answers);
        
        Exam exam = examService.getExamById(examId);
        
        // Get the submission ID for review link
        var submission = submissionRepository.findByExamIdAndStudentId(examId, student.getId())
                .orElse(null);
        
        model.addAttribute("student", student);
        model.addAttribute("exam", exam);
        model.addAttribute("score", score);
        model.addAttribute("totalMarks", exam.getTotalMarks());
        model.addAttribute("percentage", (score * 100.0) / exam.getTotalMarks());
        model.addAttribute("submissionId", submission != null ? submission.getId() : null);
        
        return "student/results";
    }
    
    /**
     * REST API to get exam questions as JSON.
     */
    @GetMapping("/api/exam/{examId}/questions")
    @ResponseBody
    public Map<String, Object> getExamQuestions(@PathVariable Long examId) {
        Exam exam = examService.getExamById(examId);
        List<Question> questions = exam.getQuestions();
        
        Map<String, Object> response = new HashMap<>();
        response.put("examId", exam.getId());
        response.put("title", exam.getTitle());
        response.put("type", exam.getType());
        response.put("duration", exam.getDurationMinutes());
        response.put("totalMarks", exam.getTotalMarks());
        response.put("questions", questions);
        
        return response;
    }
    
    /**
     * REST API for exam submission.
     */
    @PostMapping("/api/submit")
    @ResponseBody
    public Map<String, Object> submitExamApi(@RequestBody ExamSubmissionRequest request,
                                             Authentication auth) {
        Student student = studentService.findByUsername(auth.getName());
        int score = examService.submitExam(request.getExamId(), student, request.getAnswers());
        
        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        response.put("message", "Exam submitted successfully");
        response.put("timestamp", System.currentTimeMillis());
        
        return response;
    }
    
    /**
     * Review previous exam submission - shows questions, student's answers, and correct answers.
     */
    @GetMapping("/review/{submissionId}")
    public String reviewSubmission(@PathVariable Long submissionId, Model model, Authentication auth) {
        Student student = studentService.findByUsername(auth.getName());
        
        // Get the submission
        var submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        // Verify this submission belongs to the logged-in student
        if (!submission.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Unauthorized access to submission");
        }
        
        Exam exam = submission.getExam();
        String[] studentAnswers = submission.getAnswers();
        
        model.addAttribute("student", student);
        model.addAttribute("exam", exam);
        model.addAttribute("submission", submission);
        model.addAttribute("studentAnswers", studentAnswers);
        model.addAttribute("questions", exam.getQuestions());
        
        return "student/review-answers";
    }
}
