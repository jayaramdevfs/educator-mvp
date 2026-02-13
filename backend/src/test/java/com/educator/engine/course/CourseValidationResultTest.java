package com.educator.engine.course;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CourseValidationResultTest {

    @Test
    void publishableWhenNoViolations() {
        CourseValidationResult result = new CourseValidationResult();

        assertThat(result.getViolations()).isEmpty();
        assertThat(result.hasBlockingErrors()).isFalse();
        assertThat(result.isPublishable()).isTrue();
    }

    @Test
    void nonBlockingViolationDoesNotBlockPublishing() {
        CourseValidationResult result = new CourseValidationResult();
        result.addViolation(new CourseValidationResult.Violation(
                "LESSON_COUNT_TOO_LOW",
                "Too few lessons",
                CourseStructureRule.RuleSeverity.WARNING,
                false
        ));

        assertThat(result.getViolations()).hasSize(1);
        assertThat(result.hasBlockingErrors()).isFalse();
        assertThat(result.isPublishable()).isTrue();
    }

    @Test
    void blockingViolationPreventsPublishing() {
        CourseValidationResult result = new CourseValidationResult();
        result.addViolation(new CourseValidationResult.Violation(
                "MODULE_MISSING",
                "Assessment missing",
                CourseStructureRule.RuleSeverity.ERROR,
                true
        ));

        assertThat(result.hasBlockingErrors()).isTrue();
        assertThat(result.isPublishable()).isFalse();
    }

    @Test
    void violationsListIsUnmodifiable() {
        CourseValidationResult result = new CourseValidationResult();
        result.addViolation(new CourseValidationResult.Violation(
                "RULE",
                "Message",
                CourseStructureRule.RuleSeverity.INFO,
                false
        ));

        assertThatThrownBy(() -> result.getViolations().add(
                new CourseValidationResult.Violation(
                        "X",
                        "Y",
                        CourseStructureRule.RuleSeverity.INFO,
                        false
                )
        )).isInstanceOf(UnsupportedOperationException.class);
    }
}

