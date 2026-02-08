package com.educator.enrollment.entity;

import com.educator.course.Course;
import com.educator.users.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "course_id"})
        }
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────────────────────────────────────
    // Relationships
    // ─────────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // ─────────────────────────────────────────────────────────────
    // Enrollment Lifecycle
    // ─────────────────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    // ─────────────────────────────────────────────────────────────
    // Timestamps (Audit + Progress Support)
    // ─────────────────────────────────────────────────────────────

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    // ─────────────────────────────────────────────────────────────
    // Constructors
    // ─────────────────────────────────────────────────────────────

    protected Enrollment() {
        // JPA only
    }

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
        this.status = EnrollmentStatus.ACTIVE;
        this.enrolledAt = LocalDateTime.now();
    }

    // ─────────────────────────────────────────────────────────────
    // Domain Methods (NO business logic yet)
    // ─────────────────────────────────────────────────────────────

    public void markCompleted() {
        this.status = EnrollmentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void markDropped() {
        this.status = EnrollmentStatus.DROPPED;
    }

    public void touchLastAccessed() {
        this.lastAccessedAt = LocalDateTime.now();
    }

    // ─────────────────────────────────────────────────────────────
    // Getters (Setters intentionally restricted)
    // ─────────────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }
}
