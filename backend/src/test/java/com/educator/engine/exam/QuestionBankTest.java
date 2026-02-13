package com.educator.engine.exam;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionBankTest {

    @Test
    void storesCourseIdAndQuestions() {
        QuestionBank bank = new QuestionBank();
        UUID courseId = UUID.randomUUID();
        bank.setCourseId(courseId);

        QuestionBank.Question question = new QuestionBank.Question(
                UUID.randomUUID(),
                "Ancient India",
                ExamBlueprint.DifficultyLevel.MEDIUM
        );

        bank.addQuestion(question);

        assertThat(bank.getCourseId()).isEqualTo(courseId);
        assertThat(bank.getQuestions()).containsExactly(question);
    }

    @Test
    void returnsUnmodifiableQuestionList() {
        QuestionBank bank = new QuestionBank();
        bank.addQuestion(new QuestionBank.Question(
                UUID.randomUUID(),
                "Topic",
                ExamBlueprint.DifficultyLevel.EASY
        ));

        assertThatThrownBy(() -> bank.getQuestions().add(
                new QuestionBank.Question(
                        UUID.randomUUID(),
                        "Other",
                        ExamBlueprint.DifficultyLevel.HARD
                )
        )).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void questionExposesProvidedFields() {
        UUID questionId = UUID.randomUUID();
        QuestionBank.Question question = new QuestionBank.Question(
                questionId,
                "Culture",
                ExamBlueprint.DifficultyLevel.HARD
        );

        assertThat(question.getQuestionId()).isEqualTo(questionId);
        assertThat(question.getTopic()).isEqualTo("Culture");
        assertThat(question.getDifficulty()).isEqualTo(ExamBlueprint.DifficultyLevel.HARD);
    }
}

