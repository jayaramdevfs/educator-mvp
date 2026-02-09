package com.educator.exam.controller;

import com.educator.exam.entity.Exam;
import com.educator.exam.repository.ExamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Instructor-facing controller for viewing Exams.
 *
 * URL-based authorization is enforced externally.
 */
@RestController
@RequestMapping("/api/instructor/exams")
public class InstructorExamController {

    private final ExamRepository examRepository;

    public InstructorExamController(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    /**
     * Get exam by ID.
     */
    @GetMapping("/{examId}")
    public ResponseEntity<Exam> getExam(@PathVariable UUID examId) {
        return ResponseEntity.ok(
                examRepository.findById(examId)
                        .orElseThrow(() -> new IllegalArgumentException("Exam not found"))
        );
    }
}
