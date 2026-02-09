package com.educator.exam.entity;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a single option for an MCQ question.
 *
 * Correctness is explicitly stored and evaluated
 * outside this entity.
 */
@Entity
@Table(name = "exam_options")
public class ExamOption {

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Parent question.
     */
    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    /**
     * Option text shown to the learner.
     */
    @Column(name = "option_text", nullable = false, length = 2000)
    private String optionText;

    /**
     * Whether this option is correct.
     */
    @Column(name = "is_correct", nullable = false)
    private boolean correct = false;

    /**
     * Deterministic display order of the option.
     */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /* -------------------- Getters & Setters -------------------- */

    public UUID getId() {
        return id;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
