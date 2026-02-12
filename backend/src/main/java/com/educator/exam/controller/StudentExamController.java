package com.educator.exam.controller;

import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.entity.ExamAttemptAnswer;
import com.educator.exam.service.ExamAttemptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Student-facing controller for Exam attempts.
 *
 * URL-based authorization is enforced externally.
 */
@RestController
@RequestMapping("/api/student/exams")
public class StudentExamController {

    private final ExamAttemptService examAttemptService;

    public StudentExamController(ExamAttemptService examAttemptService) {
        this.examAttemptService = examAttemptService;
    }

    /**
     * Start a new exam attempt.
     */
    @PostMapping("/{examId}/start")
    public ResponseEntity<ExamAttempt> startExam(
            @PathVariable UUID examId,
            @AuthenticationPrincipal String email
    ) {
        UUID authenticatedUserId = resolveAuthenticatedUserId(email);

        return ResponseEntity.ok(
                examAttemptService.startAttempt(examId, authenticatedUserId)
        );
    }

    /**
     * Submit an exam attempt and evaluate it.
     */
    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<ExamAttempt> submitExam(
            @PathVariable UUID attemptId,
            @Valid @RequestBody List<@Valid ExamAttemptAnswer> answers
    ) {
        return ResponseEntity.ok(
                examAttemptService.submitAndEvaluateAttempt(attemptId, answers)
        );
    }

    private UUID resolveAuthenticatedUserId(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Authenticated user not found");
        }

        // Derive a stable user UUID from the authenticated email instead of accepting userId from query params.
        return UUID.nameUUIDFromBytes(
                email.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8)
        );
    }
}
