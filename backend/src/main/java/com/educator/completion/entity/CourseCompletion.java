package com.educator.completion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course_completions")
public class CourseCompletion {

    @Id
    @GeneratedValue
    private UUID id;

    // Must match Course.id (Long)
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "exam_attempt_id")
    private UUID examAttemptId;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.completedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
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
