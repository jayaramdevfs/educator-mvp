package com.educator.engine.course;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseStructureRuleTest {

    @Test
    void storesAndReturnsRuleFields() {
        CourseStructureRule rule = new CourseStructureRule();

        rule.setRuleCode("REQUIRES_ASSESSMENT_MODULE");
        rule.setDescription("Assessment module must be present");
        rule.setBlocking(true);
        rule.setSeverity(CourseStructureRule.RuleSeverity.ERROR);

        assertThat(rule.getRuleCode()).isEqualTo("REQUIRES_ASSESSMENT_MODULE");
        assertThat(rule.getDescription()).isEqualTo("Assessment module must be present");
        assertThat(rule.isBlocking()).isTrue();
        assertThat(rule.getSeverity()).isEqualTo(CourseStructureRule.RuleSeverity.ERROR);
    }

    @Test
    void supportsAllSeverityValues() {
        CourseStructureRule rule = new CourseStructureRule();

        rule.setSeverity(CourseStructureRule.RuleSeverity.INFO);
        assertThat(rule.getSeverity()).isEqualTo(CourseStructureRule.RuleSeverity.INFO);

        rule.setSeverity(CourseStructureRule.RuleSeverity.WARNING);
        assertThat(rule.getSeverity()).isEqualTo(CourseStructureRule.RuleSeverity.WARNING);

        rule.setSeverity(CourseStructureRule.RuleSeverity.ERROR);
        assertThat(rule.getSeverity()).isEqualTo(CourseStructureRule.RuleSeverity.ERROR);
    }
}

