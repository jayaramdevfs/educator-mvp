package com.educator.enrollment.controller;

import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.service.EnrollmentService;
import com.educator.users.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learner/enrollments")
public class LearnerEnrollmentController {

    private final EnrollmentService enrollmentService;

    public LearnerEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // ─────────────────────────────────────────────────────────────
    // Enroll in a course
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/course/{courseId}")
    public Enrollment enroll(@AuthenticationPrincipal User user,
                             @PathVariable Long courseId) {
        return enrollmentService.enroll(user, courseId);
    }

    // ─────────────────────────────────────────────────────────────
    // Drop enrollment
    // ─────────────────────────────────────────────────────────────

    @DeleteMapping("/{enrollmentId}")
    public void drop(@AuthenticationPrincipal User user,
                     @PathVariable Long enrollmentId) {
        enrollmentService.dropEnrollment(user, enrollmentId);
    }

    // ─────────────────────────────────────────────────────────────
    // Get my enrollments
    // ─────────────────────────────────────────────────────────────

    @GetMapping
    public List<Enrollment> myEnrollments(@AuthenticationPrincipal User user) {
        return enrollmentService.getMyEnrollments(user);
    }
}
