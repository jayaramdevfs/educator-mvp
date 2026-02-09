package com.educator.engine.exam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a logical question bank used for exam assembly.
 *
 * This class does NOT persist data and does NOT generate questions.
 * It provides a deterministic view over existing questions.
 */
public class QuestionBank {

    private UUID courseId;

    private final List<Question> questions = new ArrayList<>();

    /**
     * Represents a single question entry in the bank.
     */
    public static class Question {

        private UUID questionId;
        private String topic;
        private ExamBlueprint.DifficultyLevel difficulty;

        public Question(
                UUID questionId,
                String topic,
                ExamBlueprint.DifficultyLevel difficulty
        ) {
            this.questionId = questionId;
            this.topic = topic;
            this.difficulty = difficulty;
        }

        public UUID getQuestionId() {
            return questionId;
        }

        public String getTopic() {
            return topic;
        }

        public ExamBlueprint.DifficultyLevel getDifficulty() {
            return difficulty;
        }
    }

    /* -------------------- Bank Operations -------------------- */

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}
