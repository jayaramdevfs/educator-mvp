package com.educator.completion.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents the completion of a Course by a learner.
 *
 * This entity is created only once completion criteria
 * are satisfied and acts as the authoritative completion record.
 */
@Entity
@Table(
        name = "course_completions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"course_id", "user_id"})
        }
)
public class CourseCompletion {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Exam attempt that resulted in course completion.
     */
    @Column(name = "exam_attempt_id")
    private UUID examAttemptId;

    /**
     * Timestamp when the course was marked as completed.
     */
    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    /* -------------------- Lifecycle Hooks -------------------- */

    @PrePersist
    protected void onCreate() {
        this.completedAt = LocalDateTime.now();
    }

    /* -------------------- Getters & Setters -------------------- */

    public UUID getId() {
        return id;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getExamAttemptId() {
        return examAttemptId;
    }

    public void setExamAttemptId(UUID examAttemptId) {
        this.examAttemptId = examAttemptId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
