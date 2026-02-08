package com.educator.enrollment.controller;

import com.educator.course.lesson.Lesson;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.enrollment.service.LessonProgressService;
import com.educator.users.User;
import jakarta.persistence.EntityManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learner/progress")
public class LearnerProgressController {

    private final LessonProgressService lessonProgressService;
    private final EnrollmentRepository enrollmentRepository;
    private final EntityManager entityManager;

    public LearnerProgressController(LessonProgressService lessonProgressService,
                                     EnrollmentRepository enrollmentRepository,
                                     EntityManager entityManager) {
        this.lessonProgressService = lessonProgressService;
        this.enrollmentRepository = enrollmentRepository;
        this.entityManager = entityManager;
    }

    // ─────────────────────────────────────────────────────────────
    // Mark lesson started
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/enrollment/{enrollmentId}/start")
    public void markStarted(@AuthenticationPrincipal User user,
                            @PathVariable Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        lessonProgressService.markLessonStarted(user, enrollment);
    }

    // ─────────────────────────────────────────────────────────────
    // Mark lesson completed
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/enrollment/{enrollmentId}/lesson/{lessonId}/complete")
    public void markCompleted(@AuthenticationPrincipal User user,
                              @PathVariable Long enrollmentId,
                              @PathVariable Long lessonId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        // SAFE JPA reference — no setter, no DB hit
        Lesson lesson = entityManager.getReference(Lesson.class, lessonId);

        lessonProgressService.markLessonCompleted(user, enrollment, lesson);
    }
}
