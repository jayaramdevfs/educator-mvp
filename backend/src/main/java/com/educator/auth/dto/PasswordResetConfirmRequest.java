package com.educator.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PasswordResetConfirmRequest {

    @NotBlank(message = "Reset token is required")
    private String token;

    @NotBlank(message = "New password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters and include at least one uppercase letter and one digit"
    )
    private String newPassword;

    public PasswordResetConfirmRequest() {
    }

    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
