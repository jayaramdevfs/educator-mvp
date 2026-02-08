package com.educator.enrollment.repository;

import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.entity.EnrollmentStatus;
import com.educator.users.User;
import com.educator.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // ─────────────────────────────────────────────────────────────
    // Existence / Lookup
    // ─────────────────────────────────────────────────────────────

    Optional<Enrollment> findByUserAndCourse(User user, Course course);

    boolean existsByUserAndCourse(User user, Course course);

    // ─────────────────────────────────────────────────────────────
    // Learner Views
    // ─────────────────────────────────────────────────────────────

    List<Enrollment> findAllByUser(User user);

    List<Enrollment> findAllByUserAndStatus(User user, EnrollmentStatus status);
}
