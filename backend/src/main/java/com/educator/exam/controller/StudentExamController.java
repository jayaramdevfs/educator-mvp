package com.educator.exam.controller;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.common.security.UserIdentityUtil;
import com.educator.exam.dto.ExamAttemptReviewResponse;
import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.entity.ExamAttemptAnswer;
import com.educator.exam.service.ExamAttemptService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

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
            Authentication authentication
    ) {
        String email = resolveEmail(authentication);
        UUID authenticatedUserId = UserIdentityUtil.toStableUuid(email);

        return ResponseEntity.ok(
                examAttemptService.startAttempt(examId, authenticatedUserId, email)
        );
    }

    /**
     * Submit an exam attempt and evaluate it.
     */
    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<ExamAttempt> submitExam(
            @PathVariable UUID attemptId,
            Authentication authentication,
            @Valid @RequestBody List<@Valid ExamAttemptAnswer> answers
    ) {
        UUID authenticatedUserId = UserIdentityUtil.toStableUuid(resolveEmail(authentication));
        return ResponseEntity.ok(
                examAttemptService.submitAndEvaluateAttempt(attemptId, authenticatedUserId, answers)
        );
    }

    @GetMapping("/{examId}/attempts")
    public ResponseEntity<PaginatedResponse<ExamAttempt>> getAttemptHistory(
            @PathVariable UUID examId,
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        UUID authenticatedUserId = UserIdentityUtil.toStableUuid(resolveEmail(authentication));
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.DESC, "startedAt"));
        return ResponseEntity.ok(
                new PaginatedResponse<>(
                        examAttemptService.getAttemptHistory(examId, authenticatedUserId, pageable)
                )
        );
    }

    @GetMapping("/attempts/{attemptId}/review")
    public ResponseEntity<ExamAttemptReviewResponse> getAttemptReview(
            @PathVariable UUID attemptId,
            Authentication authentication
    ) {
        UUID authenticatedUserId = UserIdentityUtil.toStableUuid(resolveEmail(authentication));
        return ResponseEntity.ok(
                examAttemptService.getAttemptReview(attemptId, authenticatedUserId)
        );
    }

    private String resolveEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalArgumentException("Authenticated user email is required");
        }
        return authentication.getName();
    }
}
