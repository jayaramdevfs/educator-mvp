package com.educator.engine.course;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseRuleValidatorTest {

    private final CourseRuleValidator validator = new CourseRuleValidator();

    @Test
    void passesWhenModulesOrderedAndLessonCountsMeetMinimum() {
        CourseTemplate template = template(List.of("Intro", "Core", "Assessment"), true, 2);

        CourseValidationResult result = validator.validate(
                template,
                List.of("Intro", "Core", "Assessment"),
                List.of(2, 3, 2)
        );

        assertThat(result.getViolations()).isEmpty();
        assertThat(result.isPublishable()).isTrue();
    }

    @Test
    void addsBlockingViolationWhenRequiredModuleMissingAndOrderingEnforced() {
        CourseTemplate template = template(List.of("Intro", "Assessment"), true, 0);

        CourseValidationResult result = validator.validate(
                template,
                List.of("Intro", "Core"),
                List.of(1, 1)
        );

        assertThat(result.getViolations()).hasSize(1);
        assertThat(result.getViolations().get(0).getRuleCode()).isEqualTo("MODULE_ORDER_INVALID");
        assertThat(result.getViolations().get(0).isBlocking()).isTrue();
    }

    @Test
    void addsViolationForEachMissingModuleWhenOrderingNotEnforced() {
        CourseTemplate template = template(List.of("Intro", "Assessment", "Summary"), false, 0);

        CourseValidationResult result = validator.validate(
                template,
                List.of("Intro"),
                List.of(1)
        );

        assertThat(result.getViolations()).hasSize(2);
        assertThat(result.getViolations())
                .extracting(CourseValidationResult.Violation::getRuleCode)
                .containsOnly("MODULE_MISSING");
    }

    @Test
    void addsWarningsWhenLessonCountBelowMinimum() {
        CourseTemplate template = template(List.of(), false, 2);

        CourseValidationResult result = validator.validate(
                template,
                List.of("Intro", "Core"),
                List.of(1, 2)
        );

        assertThat(result.getViolations()).hasSize(1);
        CourseValidationResult.Violation violation = result.getViolations().get(0);
        assertThat(violation.getRuleCode()).isEqualTo("LESSON_COUNT_TOO_LOW");
        assertThat(violation.getSeverity()).isEqualTo(CourseStructureRule.RuleSeverity.WARNING);
        assertThat(violation.isBlocking()).isFalse();
    }

    @Test
    void ignoresModuleValidationWhenTemplateHasNoRequiredModules() {
        CourseTemplate template = template(null, true, 0);

        CourseValidationResult result = validator.validate(
                template,
                List.of("Anything"),
                List.of(1)
        );

        assertThat(result.getViolations()).isEmpty();
    }

    @Test
    void ignoresLessonValidationWhenMinimumLessThanOrEqualToZero() {
        CourseTemplate template = template(List.of("Intro"), false, 0);

        CourseValidationResult result = validator.validate(
                template,
                List.of("Intro"),
                List.of(0)
        );

        assertThat(result.getViolations()).isEmpty();
    }

    private static CourseTemplate template(List<String> requiredModules, boolean enforceOrdering, int minLessons) {
        CourseTemplate template = new CourseTemplate();
        template.setRequiredModules(requiredModules);
        template.setEnforceOrdering(enforceOrdering);
        template.setMinLessonsPerModule(minLessons);
        return template;
    }
}

