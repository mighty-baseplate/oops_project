package com.examsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dashboard Controller - redirects based on user role.
 */
@Controller
public class DashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ADMIN")) {
                    return "redirect:/admin/dashboard";
                }
            }
            return "redirect:/student/dashboard";
        }
        return "redirect:/login";
    }
}
