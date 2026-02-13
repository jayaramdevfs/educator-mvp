package com.educator.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {

    @NotBlank(message = "Token is required")
    private String token;

    public RefreshTokenRequest() {
    }

    public String getToken() {
        return token;
    }
}
