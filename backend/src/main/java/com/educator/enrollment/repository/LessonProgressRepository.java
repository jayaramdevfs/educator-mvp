package com.educator.enrollment.repository;

import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.entity.LessonProgress;
import com.educator.course.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    // ─────────────────────────────────────────────────────────────
    // Lookup
    // ─────────────────────────────────────────────────────────────

    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);

    List<LessonProgress> findAllByEnrollment(Enrollment enrollment);

    long countByEnrollmentAndCompletedTrue(Enrollment enrollment);
}
