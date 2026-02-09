package com.educator.exam.entity;

import com.educator.exam.enums.ExamStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Core Exam entity.
 *
 * An Exam is bound to exactly one Course and defines
 * configuration and lifecycle only.
 *
 * Evaluation logic is handled outside this entity.
 */
@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Course to which this exam belongs.
     * One-to-one by contract (enforced at service layer).
     */
    @Column(name = "course_id", nullable = false, unique = true)
    private UUID courseId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    /**
     * Instructions shown to the learner before starting the exam.
     */
    @Column(length = 4000)
    private String instructions;

    /**
     * Short rules summary (e.g., attempts, timing).
     */
    @Column(length = 2000)
    private String rulesSummary;

    /**
     * Percentage required to pass the exam.
     */
    @Column(name = "pass_percentage", nullable = false)
    private Integer passPercentage;

    /**
     * Maximum attempts allowed.
     * Null means unlimited attempts.
     */
    @Column(name = "max_attempts")
    private Integer maxAttempts;

    /**
     * Time limit in minutes.
     * Null means no time limit.
     */
    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    /**
     * Whether questions should be shuffled per attempt.
     */
    @Column(name = "shuffle_questions", nullable = false)
    private boolean shuffleQuestions = false;

    /**
     * Whether options should be shuffled per question.
     */
    @Column(name = "shuffle_options", nullable = false)
    private boolean shuffleOptions = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamStatus status = ExamStatus.DRAFT;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* -------------------- Lifecycle Hooks -------------------- */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getRulesSummary() {
        return rulesSummary;
    }

    public void setRulesSummary(String rulesSummary) {
        this.rulesSummary = rulesSummary;
    }

    public Integer getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(Integer passPercentage) {
        this.passPercentage = passPercentage;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public boolean isShuffleQuestions() {
        return shuffleQuestions;
    }

    public void setShuffleQuestions(boolean shuffleQuestions) {
        this.shuffleQuestions = shuffleQuestions;
    }

    public boolean isShuffleOptions() {
        return shuffleOptions;
    }

    public void setShuffleOptions(boolean shuffleOptions) {
        this.shuffleOptions = shuffleOptions;
    }

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
