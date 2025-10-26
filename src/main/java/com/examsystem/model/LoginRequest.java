package com.examsystem.model;

import lombok.Data;

/**
 * DTO for login request.
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
