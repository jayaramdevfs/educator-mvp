package com.educator.profile.dto;

import java.util.Set;

public class ProfileResponse {

    private Long id;
    private String email;
    private Set<String> roles;

    public ProfileResponse(Long id, String email, Set<String> roles) {
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
