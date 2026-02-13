package com.educator.auth.dto;

public class PasswordResetRequestResponse {

    private final String message;
    private final String token;

    public PasswordResetRequestResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
