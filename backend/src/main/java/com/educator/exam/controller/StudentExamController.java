package com.educator.exam.controller;

import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.entity.ExamAttemptAnswer;
import com.educator.exam.service.ExamAttemptService;
import org.springframework.http.ResponseEntity;
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
            @RequestParam UUID userId
    ) {
        return ResponseEntity.ok(
                examAttemptService.startAttempt(examId, userId)
        );
    }

    /**
     * Submit an exam attempt and evaluate it.
     */
    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<ExamAttempt> submitExam(
            @PathVariable UUID attemptId,
            @RequestBody List<ExamAttemptAnswer> answers
    ) {
        return ResponseEntity.ok(
                examAttemptService.submitAndEvaluateAttempt(attemptId, answers)
        );
    }
}
