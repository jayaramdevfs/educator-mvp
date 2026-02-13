package com.educator.enrollment.controller;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.service.EnrollmentService;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/learner/enrollments")
public class LearnerEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;

    public LearnerEnrollmentController(
            EnrollmentService enrollmentService,
            UserRepository userRepository
    ) {
        this.enrollmentService = enrollmentService;
        this.userRepository = userRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // Enroll in a course
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/course/{courseId}")
    public Enrollment enroll(
            Authentication authentication,
            @PathVariable Long courseId
    ) {
        User user = resolveAuthenticatedUser(authentication);
        return enrollmentService.enroll(user, courseId);
    }

    @GetMapping
    public PaginatedResponse<Enrollment> getMyEnrollments(
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        User user = resolveAuthenticatedUser(authentication);
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.DESC, "enrolledAt"));
        return new PaginatedResponse<>(enrollmentService.getMyEnrollments(user, pageable));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> dropEnrollment(
            Authentication authentication,
            @PathVariable Long enrollmentId
    ) {
        User user = resolveAuthenticatedUser(authentication);
        enrollmentService.dropEnrollment(user, enrollmentId);
        return ResponseEntity.noContent().build();
    }

    private User resolveAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName(); // JWT subject
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"
                ));
    }
}
