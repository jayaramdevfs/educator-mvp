package com.educator.enrollment.service;

import com.educator.course.lesson.Lesson;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.entity.LessonProgress;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.enrollment.repository.LessonProgressRepository;
import com.educator.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentRepository enrollmentRepository;

    public LessonProgressService(LessonProgressRepository lessonProgressRepository,
                                 EnrollmentRepository enrollmentRepository) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // Mark lesson started (touch only)
    // ─────────────────────────────────────────────────────────────

    public void markLessonStarted(User user, Enrollment enrollment) {
        verifyOwnership(user, enrollment);
        enrollment.touchLastAccessed();
        enrollmentRepository.save(enrollment);
    }

    // ─────────────────────────────────────────────────────────────
    // Mark lesson completed (IDEMPOTENT)
    // ─────────────────────────────────────────────────────────────

    public void markLessonCompleted(User user, Enrollment enrollment, Lesson lesson) {
        verifyOwnership(user, enrollment);

        LessonProgress progress = lessonProgressRepository
                .findByEnrollmentAndLesson(enrollment, lesson)
                .orElseGet(() -> new LessonProgress(enrollment, lesson));

        progress.markCompleted();
        lessonProgressRepository.save(progress);

        enrollment.touchLastAccessed();
        enrollmentRepository.save(enrollment);

        // NOTE:
        // Auto-complete enrollment will be triggered later
        // once lesson-count queries are verified safe
    }

    // ─────────────────────────────────────────────────────────────
    // Ownership guard
    // ─────────────────────────────────────────────────────────────

    private void verifyOwnership(User user, Enrollment enrollment) {
        if (!enrollment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Unauthorized enrollment access");
        }
    }
}
