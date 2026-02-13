package com.educator.users.dto;

import java.util.Set;

public class AdminUserResponse {

    private Long id;
    private String email;
    private Set<String> roles;

    public AdminUserResponse(Long id, String email, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
