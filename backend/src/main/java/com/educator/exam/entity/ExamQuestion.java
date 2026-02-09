package com.educator.exam.entity;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a single MCQ question belonging to an Exam.
 *
 * Options and correct answers are defined separately.
 * This entity contains no evaluation logic.
 */
@Entity
@Table(name = "exam_questions")
public class ExamQuestion {

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Parent exam.
     */
    @Column(name = "exam_id", nullable = false)
    private UUID examId;

    /**
     * Question text shown to the learner.
     */
    @Column(name = "question_text", nullable = false, length = 4000)
    private String questionText;

    /**
     * Deterministic display order of the question within the exam.
     */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /**
     * Optional explanation shown after evaluation (future-safe).
     */
    @Column(name = "explanation", length = 4000)
    private String explanation;

    /* -------------------- Getters & Setters -------------------- */

    public UUID getId() {
        return id;
    }

    public UUID getExamId() {
        return examId;
    }

    public void setExamId(UUID examId) {
        this.examId = examId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
