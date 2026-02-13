package com.educator.enrollment.service;

import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonRepository;
import com.educator.completion.repository.CourseCompletionRepository;
import com.educator.common.security.UserIdentityUtil;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.entity.LessonProgress;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.enrollment.repository.LessonProgressRepository;
import com.educator.exam.repository.ExamAttemptRepository;
import com.educator.exam.repository.ExamRepository;
import com.educator.exam.enums.AttemptStatus;
import com.educator.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final CourseCompletionRepository courseCompletionRepository;

    public LessonProgressService(
            LessonProgressRepository lessonProgressRepository,
            EnrollmentRepository enrollmentRepository,
            LessonRepository lessonRepository,
            ExamRepository examRepository,
            ExamAttemptRepository examAttemptRepository,
            CourseCompletionRepository courseCompletionRepository
    ) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.examRepository = examRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.courseCompletionRepository = courseCompletionRepository;
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
        autoCompleteEnrollmentIfEligible(user, enrollment);
    }

    // ─────────────────────────────────────────────────────────────
    // Ownership guard
    // ─────────────────────────────────────────────────────────────

    private void verifyOwnership(User user, Enrollment enrollment) {
        if (!enrollment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Unauthorized enrollment access");
        }
    }

    private void autoCompleteEnrollmentIfEligible(User user, Enrollment enrollment) {
        long totalLessons = lessonRepository.findAllByCourseFlatOrdered(enrollment.getCourse()).size();
        if (totalLessons == 0) {
            return;
        }

        long completedLessons = lessonProgressRepository.countByEnrollmentAndCompletedTrue(enrollment);
        if (completedLessons < totalLessons) {
            return;
        }

        UUID userUuid = UserIdentityUtil.toStableUuid(user.getEmail());
        Long courseId = enrollment.getCourse().getId();

        boolean hasCompletion = courseCompletionRepository.findByCourseIdAndUserId(courseId, userUuid).isPresent();
        if (!hasCompletion) {
            return;
        }

        boolean hasEvaluatedExamAttempt = examRepository.findByCourseId(courseId)
                .map(exam -> examAttemptRepository.findTopByExamIdAndUserIdAndStatusOrderByEvaluatedAtDesc(
                        exam.getId(),
                        userUuid,
                        AttemptStatus.EVALUATED
                ).isPresent())
                .orElse(false);

        if (hasEvaluatedExamAttempt) {
            enrollment.markCompleted();
            enrollmentRepository.save(enrollment);
        }
    }
}
