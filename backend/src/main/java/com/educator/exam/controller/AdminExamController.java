package com.educator.exam.controller;

import com.educator.exam.entity.Exam;
import com.educator.exam.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Admin-only controller for managing Exams.
 *
 * URL-based authorization is enforced externally.
 */
@RestController
@RequestMapping("/api/admin/exams")
public class AdminExamController {

    private final ExamService examService;

    public AdminExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * Create a new exam for a course.
     */
    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        return ResponseEntity.ok(examService.createExam(exam));
    }

    /**
     * Publish an exam.
     */
    @PostMapping("/{examId}/publish")
    public ResponseEntity<Exam> publishExam(@PathVariable UUID examId) {
        return ResponseEntity.ok(examService.publishExam(examId));
    }

    /**
     * Archive an exam.
     */
    @PostMapping("/{examId}/archive")
    public ResponseEntity<Exam> archiveExam(@PathVariable UUID examId) {
        return ResponseEntity.ok(examService.archiveExam(examId));
    }
}
