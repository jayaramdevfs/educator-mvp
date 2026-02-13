package com.educator.engine.exam;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExamRuleEvaluatorTest {

    private final ExamRuleEvaluator evaluator = new ExamRuleEvaluator();

    @Test
    void addsCourseMismatchErrorWhenBlueprintAndBankCourseDiffer() {
        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setCourseId(UUID.randomUUID());
        blueprint.setTotalQuestions(1);

        QuestionBank bank = new QuestionBank();
        bank.setCourseId(UUID.randomUUID());
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "History",
                ExamBlueprint.DifficultyLevel.EASY
        ));

        ExamRuleEvaluator.EvaluationResult result = evaluator.evaluate(blueprint, bank);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).containsKey("COURSE_MISMATCH");
    }

    @Test
    void addsInsufficientQuestionsErrorWhenBankTooSmall() {
        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setTotalQuestions(3);

        QuestionBank bank = new QuestionBank();
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "History",
                ExamBlueprint.DifficultyLevel.EASY
        ));

        ExamRuleEvaluator.EvaluationResult result = evaluator.evaluate(blueprint, bank);

        assertThat(result.getErrors()).containsKey("INSUFFICIENT_QUESTIONS");
    }

    @Test
    void addsDifficultyShortageWhenDistributionCannotBeMet() {
        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setTotalQuestions(2);
        blueprint.setDifficultyDistribution(Map.of(
                ExamBlueprint.DifficultyLevel.HARD, 2
        ));

        QuestionBank bank = new QuestionBank();
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "History",
                ExamBlueprint.DifficultyLevel.EASY
        ));
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "Culture",
                ExamBlueprint.DifficultyLevel.MEDIUM
        ));

        ExamRuleEvaluator.EvaluationResult result = evaluator.evaluate(blueprint, bank);

        assertThat(result.getErrors()).containsKey("DIFFICULTY_SHORTAGE");
    }

    @Test
    void addsTopicShortageWhenDistributionCannotBeMet() {
        ExamBlueprint blueprint = new ExamBlueprint();
        blueprint.setTotalQuestions(2);
        blueprint.setTopicDistribution(Map.of("Vedic", 2));

        QuestionBank bank = new QuestionBank();
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "Modern",
                ExamBlueprint.DifficultyLevel.EASY
        ));
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "Modern",
                ExamBlueprint.DifficultyLevel.MEDIUM
        ));

        ExamRuleEvaluator.EvaluationResult result = evaluator.evaluate(blueprint, bank);

        assertThat(result.getErrors()).containsKey("TOPIC_SHORTAGE");
    }

    @Test
    void returnsNoErrorsWhenBlueprintIsFeasible() {
        ExamBlueprint blueprint = new ExamBlueprint();
        UUID courseId = UUID.randomUUID();
        blueprint.setCourseId(courseId);
        blueprint.setTotalQuestions(2);
        blueprint.setDifficultyDistribution(Map.of(
                ExamBlueprint.DifficultyLevel.EASY, 1,
                ExamBlueprint.DifficultyLevel.MEDIUM, 1
        ));
        blueprint.setTopicDistribution(Map.of("History", 1, "Culture", 1));

        QuestionBank bank = new QuestionBank();
        bank.setCourseId(courseId);
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "History",
                ExamBlueprint.DifficultyLevel.EASY
        ));
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "Culture",
                ExamBlueprint.DifficultyLevel.MEDIUM
        ));

        ExamRuleEvaluator.EvaluationResult result = evaluator.evaluate(blueprint, bank);

        assertThat(result.hasErrors()).isFalse();
        assertThat(result.getErrors()).isEmpty();
    }
}

