package com.educator.enrollment.service;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.CourseStatus;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.entity.EnrollmentStatus;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // Enroll
    // ─────────────────────────────────────────────────────────────

    public Enrollment enroll(User user, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.isDeleted()) {
            throw new IllegalStateException("Course is deleted");
        }

        if (course.isArchived()) {
            throw new IllegalStateException("Course is archived");
        }

        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new IllegalStateException("Course is not published");
        }

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new IllegalStateException("User already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(user, course);
        return enrollmentRepository.save(enrollment);
    }

    // ─────────────────────────────────────────────────────────────
    // Unenroll (Drop)
    // ─────────────────────────────────────────────────────────────

    public void dropEnrollment(User user, Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        if (!enrollment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Unauthorized enrollment access");
        }

        enrollment.markDropped();
        enrollmentRepository.save(enrollment);
    }

    // ─────────────────────────────────────────────────────────────
    // Queries (Learner View)
    // ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Enrollment> getMyEnrollments(User user) {
        return enrollmentRepository.findAllByUser(user).stream()
                .filter(e -> !e.getCourse().isDeleted() &&
                        !e.getCourse().isArchived() &&
                        e.getCourse().getStatus() == CourseStatus.PUBLISHED)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<Enrollment> getMyEnrollments(User user, Pageable pageable) {
        return enrollmentRepository.findAllByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getMyActiveEnrollments(User user) {
        return enrollmentRepository.findAllByUserAndStatus(user, EnrollmentStatus.ACTIVE);
    }

    // ─────────────────────────────────────────────────────────────
    // Internal lifecycle hook (used by ProgressService later)
    // ─────────────────────────────────────────────────────────────

    void markCompleted(Enrollment enrollment) {
        enrollment.markCompleted();
        enrollmentRepository.save(enrollment);
    }
}
