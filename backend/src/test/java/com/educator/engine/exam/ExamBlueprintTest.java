package com.educator.engine.exam;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExamBlueprintTest {

    @Test
    void storesAndReturnsBlueprintProperties() {
        ExamBlueprint blueprint = new ExamBlueprint();
        UUID id = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        blueprint.setId(id);
        blueprint.setCourseId(courseId);
        blueprint.setTotalQuestions(50);
        blueprint.setPassPercentage(70);
        blueprint.setDifficultyDistribution(Map.of(
                ExamBlueprint.DifficultyLevel.EASY, 20,
                ExamBlueprint.DifficultyLevel.MEDIUM, 20,
                ExamBlueprint.DifficultyLevel.HARD, 10
        ));
        blueprint.setTopicDistribution(Map.of("History", 30, "Culture", 20));
        blueprint.setStrict(true);

        assertThat(blueprint.getId()).isEqualTo(id);
        assertThat(blueprint.getCourseId()).isEqualTo(courseId);
        assertThat(blueprint.getTotalQuestions()).isEqualTo(50);
        assertThat(blueprint.getPassPercentage()).isEqualTo(70);
        assertThat(blueprint.getDifficultyDistribution()).hasSize(3);
        assertThat(blueprint.getTopicDistribution()).hasSize(2);
        assertThat(blueprint.isStrict()).isTrue();
    }
}

