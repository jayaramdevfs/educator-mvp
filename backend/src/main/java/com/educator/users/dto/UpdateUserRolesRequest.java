package com.educator.users.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateUserRolesRequest {

    @NotEmpty(message = "At least one role is required")
    private List<String> roles;

    public UpdateUserRolesRequest() {
    }

    public List<String> getRoles() {
        return roles;
    }
}
