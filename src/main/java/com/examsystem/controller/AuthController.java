package com.examsystem.controller;

import com.examsystem.entity.Student;
import com.examsystem.model.LoginRequest;
import com.examsystem.security.JwtUtil;
import com.examsystem.service.IStudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller.
 * Handles login, registration, and JWT token generation.
 */
@Controller
public class AuthController {
    
    private final IStudentService studentService;
    private final JwtUtil jwtUtil;
    
    public AuthController(IStudentService studentService, JwtUtil jwtUtil) {
        this.studentService = studentService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerStudent(@RequestParam String name,
                                  @RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {
        try {
            studentService.registerStudent(name, username, password);
            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    
    /**
     * REST API for JWT token generation.
     */
    @PostMapping("/api/auth/login")
    @ResponseBody
    public Map<String, String> login(@RequestBody LoginRequest request) {
        Map<String, String> response = new HashMap<>();
        
        if (studentService.authenticate(request.getUsername(), request.getPassword())) {
            Student student = studentService.findByUsername(request.getUsername());
            String token = jwtUtil.generateToken(student.getUsername(), student.getRole().name());
            
            response.put("token", token);
            response.put("username", student.getUsername());
            response.put("role", student.getRole().name());
            response.put("message", "Login successful");
        } else {
            response.put("error", "Invalid credentials");
        }
        
        return response;
    }
}
