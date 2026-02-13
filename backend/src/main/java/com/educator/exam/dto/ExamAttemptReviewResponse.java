package com.educator.exam.dto;

import java.util.List;
import java.util.UUID;

public class ExamAttemptReviewResponse {

    private UUID attemptId;
    private UUID examId;
    private Integer scorePercentage;
    private Boolean passed;
    private List<QuestionReview> questions;

    public ExamAttemptReviewResponse(
            UUID attemptId,
            UUID examId,
            Integer scorePercentage,
            Boolean passed,
            List<QuestionReview> questions
    ) {
        this.attemptId = attemptId;
        this.examId = examId;
        this.scorePercentage = scorePercentage;
        this.passed = passed;
        this.questions = questions;
    }

    public UUID getAttemptId() {
        return attemptId;
    }

    public UUID getExamId() {
        return examId;
    }

    public Integer getScorePercentage() {
        return scorePercentage;
    }

    public Boolean getPassed() {
        return passed;
    }

    public List<QuestionReview> getQuestions() {
        return questions;
    }

    public static class QuestionReview {
        private UUID questionId;
        private String questionText;
        private String explanation;
        private UUID selectedOptionId;
        private String selectedOptionText;
        private UUID correctOptionId;
        private String correctOptionText;
        private boolean correct;

        public QuestionReview(
                UUID questionId,
                String questionText,
                String explanation,
                UUID selectedOptionId,
                String selectedOptionText,
                UUID correctOptionId,
                String correctOptionText,
                boolean correct
        ) {
            this.questionId = questionId;
            this.questionText = questionText;
            this.explanation = explanation;
            this.selectedOptionId = selectedOptionId;
            this.selectedOptionText = selectedOptionText;
            this.correctOptionId = correctOptionId;
            this.correctOptionText = correctOptionText;
            this.correct = correct;
        }

        public UUID getQuestionId() {
            return questionId;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String getExplanation() {
            return explanation;
        }

        public UUID getSelectedOptionId() {
            return selectedOptionId;
        }

        public String getSelectedOptionText() {
            return selectedOptionText;
        }

        public UUID getCorrectOptionId() {
            return correctOptionId;
        }

        public String getCorrectOptionText() {
            return correctOptionText;
        }

        public boolean isCorrect() {
            return correct;
        }
    }
}
