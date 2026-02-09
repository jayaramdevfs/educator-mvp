package com.educator.engine.exam;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Evaluates whether an ExamBlueprint can be satisfied
 * by a given QuestionBank.
 */
public class ExamRuleEvaluator {

    /**
     * Evaluates blueprint feasibility.
     *
     * @param blueprint exam blueprint
     * @param bank      available question bank
     * @return evaluation result
     */
    public EvaluationResult evaluate(
            ExamBlueprint blueprint,
            QuestionBank bank
    ) {
        EvaluationResult result = new EvaluationResult();

        // Rule: course binding must match
        if (blueprint.getCourseId() != null
                && bank.getCourseId() != null
                && !blueprint.getCourseId().equals(bank.getCourseId())) {

            result.addError(
                    "COURSE_MISMATCH",
                    "Question bank does not belong to the required course"
            );
            return result;
        }

        // Rule: total questions availability
        if (bank.getQuestions().size() < blueprint.getTotalQuestions()) {
            result.addError(
                    "INSUFFICIENT_QUESTIONS",
                    "Question bank contains fewer questions than required"
            );
        }

        // Rule: difficulty distribution
        if (blueprint.getDifficultyDistribution() != null) {
            validateDifficultyDistribution(blueprint, bank, result);
        }

        // Rule: topic distribution
        if (blueprint.getTopicDistribution() != null) {
            validateTopicDistribution(blueprint, bank, result);
        }

        return result;
    }

    /* -------------------- Rule Implementations -------------------- */

    private void validateDifficultyDistribution(
            ExamBlueprint blueprint,
            QuestionBank bank,
            EvaluationResult result
    ) {
        Map<ExamBlueprint.DifficultyLevel, Integer> available =
                new EnumMap<>(ExamBlueprint.DifficultyLevel.class);

        for (QuestionBank.Question q : bank.getQuestions()) {
            available.merge(q.getDifficulty(), 1, Integer::sum);
        }

        for (Map.Entry<ExamBlueprint.DifficultyLevel, Integer> entry
                : blueprint.getDifficultyDistribution().entrySet()) {

            int availableCount = available.getOrDefault(entry.getKey(), 0);
            if (availableCount < entry.getValue()) {
                result.addError(
                        "DIFFICULTY_SHORTAGE",
                        "Not enough " + entry.getKey() + " questions"
                );
            }
        }
    }

    private void validateTopicDistribution(
            ExamBlueprint blueprint,
            QuestionBank bank,
            EvaluationResult result
    ) {
        Map<String, Integer> available = new HashMap<>();

        for (QuestionBank.Question q : bank.getQuestions()) {
            available.merge(q.getTopic(), 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> entry
                : blueprint.getTopicDistribution().entrySet()) {

            int availableCount = available.getOrDefault(entry.getKey(), 0);
            if (availableCount < entry.getValue()) {
                result.addError(
                        "TOPIC_SHORTAGE",
                        "Not enough questions for topic: " + entry.getKey()
                );
            }
        }
    }

    /* -------------------- Evaluation Result -------------------- */

    public static class EvaluationResult {

        private final Map<String, String> errors = new HashMap<>();

        public void addError(String code, String message) {
            errors.put(code, message);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public Map<String, String> getErrors() {
            return errors;
        }
    }
}
