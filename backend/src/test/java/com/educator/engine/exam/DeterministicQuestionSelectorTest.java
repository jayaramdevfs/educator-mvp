package com.educator.engine.exam;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class DeterministicQuestionSelectorTest {

    private final DeterministicQuestionSelector selector = new DeterministicQuestionSelector();

    @Test
    void selectsQuestionsByTopicAndDifficultyAndFillsWhenNotStrict() {
        QuestionBank bank = bankWithQuestions(
                question("history-a", "History", ExamBlueprint.DifficultyLevel.EASY),
                question("history-b", "History", ExamBlueprint.DifficultyLevel.MEDIUM),
                question("culture-a", "Culture", ExamBlueprint.DifficultyLevel.MEDIUM),
                question("culture-b", "Culture", ExamBlueprint.DifficultyLevel.HARD)
        );

        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setTotalQuestions(3);
        blueprint.setStrict(false);
        blueprint.setTopicDistribution(Map.of("History", 1));
        blueprint.setDifficultyDistribution(Map.of(
                ExamBlueprint.DifficultyLevel.MEDIUM, 1
        ));

        List<QuestionBank.Question> selected = selector.select(blueprint, bank);

        assertThat(selected).hasSize(3);
        assertThat(selected.stream().map(QuestionBank.Question::getTopic))
                .contains("History");
    }

    @Test
    void doesNotFillRemainingSlotsWhenStrict() {
        QuestionBank bank = bankWithQuestions(
                question("history-a", "History", ExamBlueprint.DifficultyLevel.EASY),
                question("culture-a", "Culture", ExamBlueprint.DifficultyLevel.MEDIUM)
        );

        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setTotalQuestions(4);
        blueprint.setStrict(true);
        blueprint.setTopicDistribution(Map.of("History", 1));
        blueprint.setDifficultyDistribution(Map.of(ExamBlueprint.DifficultyLevel.MEDIUM, 1));

        List<QuestionBank.Question> selected = selector.select(blueprint, bank);

        assertThat(selected).hasSize(2);
    }

    @Test
    void selectionIsDeterministicForSameInput() {
        QuestionBank bank = bankWithQuestions(
                question("q1", "History", ExamBlueprint.DifficultyLevel.EASY),
                question("q2", "History", ExamBlueprint.DifficultyLevel.MEDIUM),
                question("q3", "Culture", ExamBlueprint.DifficultyLevel.HARD),
                question("q4", "Culture", ExamBlueprint.DifficultyLevel.MEDIUM)
        );

        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setTotalQuestions(3);
        blueprint.setStrict(false);
        blueprint.setTopicDistribution(Map.of("History", 1, "Culture", 1));
        blueprint.setDifficultyDistribution(Map.of(ExamBlueprint.DifficultyLevel.MEDIUM, 1));

        List<QuestionBank.Question> first = selector.select(blueprint, bank);
        List<QuestionBank.Question> second = selector.select(blueprint, bank);

        assertThat(first.stream().map(q -> q.getQuestionId().toString()).toList())
                .isEqualTo(second.stream().map(q -> q.getQuestionId().toString()).toList());
    }

    @Test
    void avoidsDuplicateQuestionsAcrossSelectionSteps() {
        QuestionBank bank = bankWithQuestions(
                question("dup-1", "History", ExamBlueprint.DifficultyLevel.MEDIUM),
                question("dup-2", "History", ExamBlueprint.DifficultyLevel.MEDIUM),
                question("dup-3", "History", ExamBlueprint.DifficultyLevel.MEDIUM)
        );

        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setTotalQuestions(3);
        blueprint.setStrict(false);
        blueprint.setTopicDistribution(Map.of("History", 2));
        blueprint.setDifficultyDistribution(Map.of(ExamBlueprint.DifficultyLevel.MEDIUM, 2));

        List<QuestionBank.Question> selected = selector.select(blueprint, bank);
        List<UUID> ids = selected.stream().map(QuestionBank.Question::getQuestionId).collect(Collectors.toList());

        assertThat(ids).doesNotHaveDuplicates();
    }

    private static QuestionBank bankWithQuestions(QuestionBank.Question... questions) {
        QuestionBank bank = new QuestionBank();
        for (QuestionBank.Question question : questions) {
            bank.addQuestion(question);
        }
        return bank;
    }

    private static QuestionBank.Question question(
            String stableSeed,
            String topic,
            ExamBlueprint.DifficultyLevel level
    ) {
        return new QuestionBank.Question(
                UUID.nameUUIDFromBytes(stableSeed.getBytes()),
                topic,
                level
        );
    }
}

