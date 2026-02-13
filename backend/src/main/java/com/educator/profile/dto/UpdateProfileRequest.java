package com.educator.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    public UpdateProfileRequest() {
    }

    public String getEmail() {
        return email;
    }
}
