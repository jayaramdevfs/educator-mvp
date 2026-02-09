package com.educator.engine.exam;

import java.util.Map;
import java.util.UUID;

/**
 * Defines the structure and constraints of an Exam.
 *
 * This blueprint does not contain questions.
 * It defines how questions should be selected.
 */
public class ExamBlueprint {

    private UUID id;

    /**
     * Optional course binding.
     * If set, questions must come from this course's question bank.
     */
    private UUID courseId;

    /**
     * Total number of questions required.
     */
    private int totalQuestions;

    /**
     * Required pass percentage.
     */
    private int passPercentage;

    /**
     * Difficulty distribution.
     * Example: EASY=20, MEDIUM=20, HARD=10
     */
    private Map<DifficultyLevel, Integer> difficultyDistribution;

    /**
     * Topic-wise question distribution.
     * Example: "Vedic Society" -> 10 questions
     */
    private Map<String, Integer> topicDistribution;

    /**
     * Whether the exam must strictly satisfy the blueprint.
     * If true, exam creation fails when constraints cannot be met.
     */
    private boolean strict;

    public enum DifficultyLevel {
        EASY,
        MEDIUM,
        HARD
    }

    /* -------------------- Getters & Setters -------------------- */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(int passPercentage) {
        this.passPercentage = passPercentage;
    }

    public Map<DifficultyLevel, Integer> getDifficultyDistribution() {
        return difficultyDistribution;
    }

    public void setDifficultyDistribution(
            Map<DifficultyLevel, Integer> difficultyDistribution
    ) {
        this.difficultyDistribution = difficultyDistribution;
    }

    public Map<String, Integer> getTopicDistribution() {
        return topicDistribution;
    }

    public void setTopicDistribution(Map<String, Integer> topicDistribution) {
        this.topicDistribution = topicDistribution;
    }

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }
}
