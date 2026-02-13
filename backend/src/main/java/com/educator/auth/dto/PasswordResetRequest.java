package com.educator.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    public PasswordResetRequest() {
    }

    public String getEmail() {
        return email;
    }
}
