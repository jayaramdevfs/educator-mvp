package com.educator.engine.exam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Deterministically selects questions from a QuestionBank
 * based on an ExamBlueprint.
 */
public class DeterministicQuestionSelector {

    /**
     * Selects questions according to blueprint constraints.
     *
     * @param blueprint exam blueprint
     * @param bank      question bank
     * @return ordered list of selected question IDs
     */
    public List<QuestionBank.Question> select(
            ExamBlueprint blueprint,
            QuestionBank bank
    ) {
        List<QuestionBank.Question> selected = new ArrayList<>();

        // Step 1: filter by course binding if required
        List<QuestionBank.Question> pool = new ArrayList<>(bank.getQuestions());

        // Step 2: deterministic ordering (topic -> difficulty -> ID)
        pool.sort(
                Comparator
                        .comparing(QuestionBank.Question::getTopic)
                        .thenComparing(QuestionBank.Question::getDifficulty)
                        .thenComparing(q -> q.getQuestionId().toString())
        );

        // Step 3: topic-based selection
        if (blueprint.getTopicDistribution() != null) {
            for (Map.Entry<String, Integer> entry
                    : blueprint.getTopicDistribution().entrySet()) {

                String topic = entry.getKey();
                int required = entry.getValue();

                for (QuestionBank.Question q : pool) {
                    if (required == 0) break;
                    if (topic.equals(q.getTopic()) && !selected.contains(q)) {
                        selected.add(q);
                        required--;
                    }
                }
            }
        }

        // Step 4: difficulty-based selection (fill remaining)
        if (selected.size() < blueprint.getTotalQuestions()
                && blueprint.getDifficultyDistribution() != null) {

            for (Map.Entry<ExamBlueprint.DifficultyLevel, Integer> entry
                    : blueprint.getDifficultyDistribution().entrySet()) {

                ExamBlueprint.DifficultyLevel level = entry.getKey();
                int required = entry.getValue();

                for (QuestionBank.Question q : pool) {
                    if (selected.size() >= blueprint.getTotalQuestions()) break;
                    if (required == 0) break;

                    if (q.getDifficulty() == level && !selected.contains(q)) {
                        selected.add(q);
                        required--;
                    }
                }
            }
        }

        // Step 5: final fill (if still short and not strict)
        if (!blueprint.isStrict()
                && selected.size() < blueprint.getTotalQuestions()) {

            for (QuestionBank.Question q : pool) {
                if (selected.size() >= blueprint.getTotalQuestions()) break;
                if (!selected.contains(q)) {
                    selected.add(q);
                }
            }
        }

        return selected;
    }
}
