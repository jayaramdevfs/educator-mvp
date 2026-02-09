package com.educator.exam.entity;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a learner's answer to a specific question
 * within an exam attempt.
 *
 * This entity stores raw answer data only.
 * Evaluation is handled separately.
 */
@Entity
@Table(name = "exam_attempt_answers")
public class ExamAttemptAnswer {

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Parent exam attempt.
     */
    @Column(name = "attempt_id", nullable = false)
    private UUID attemptId;

    /**
     * Question being answered.
     */
    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    /**
     * Selected option.
     *
     * For multi-correct MCQs in the future,
     * this model can be extended to allow multiple rows
     * per question per attempt.
     */
    @Column(name = "selected_option_id", nullable = false)
    private UUID selectedOptionId;

    /* -------------------- Getters & Setters -------------------- */

    public UUID getId() {
        return id;
    }

    public UUID getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(UUID attemptId) {
        this.attemptId = attemptId;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public UUID getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(UUID selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }
}
