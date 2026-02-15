package com.educator.security.service;

import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    // Temporary implementation
    // Subscription logic will be added in next sprint

    public boolean canAccessCourse(Long userId, Long courseId) {
        return true; // Allow all for now
    }

    public boolean canAccessExam(Long userId, java.util.UUID examId) {
        return true; // Allow all for now
    }

    public boolean hasActiveSubscription(Long userId) {
        return true; // Placeholder
    }
}
