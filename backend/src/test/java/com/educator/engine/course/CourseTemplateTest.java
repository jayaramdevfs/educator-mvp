package com.educator.engine.course;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CourseTemplateTest {

    @Test
    void storesAllTemplateProperties() {
        CourseTemplate template = new CourseTemplate();
        UUID id = UUID.randomUUID();

        template.setId(id);
        template.setName("Certification Template");
        template.setRequiredModules(List.of("Intro", "Assessment"));
        template.setAssessmentRequired(true);
        template.setMinLessonsPerModule(3);
        template.setEnforceOrdering(true);
        template.setAllowPublishOnValidationFailure(true);

        assertThat(template.getId()).isEqualTo(id);
        assertThat(template.getName()).isEqualTo("Certification Template");
        assertThat(template.getRequiredModules()).containsExactly("Intro", "Assessment");
        assertThat(template.isAssessmentRequired()).isTrue();
        assertThat(template.getMinLessonsPerModule()).isEqualTo(3);
        assertThat(template.isEnforceOrdering()).isTrue();
        assertThat(template.isAllowPublishOnValidationFailure()).isTrue();
    }

    @Test
    void defaultsAllowPublishOnValidationFailureToFalse() {
        CourseTemplate template = new CourseTemplate();

        assertThat(template.isAllowPublishOnValidationFailure()).isFalse();
    }
}

