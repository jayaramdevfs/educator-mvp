package com.educator.engine.completion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificateEligibilityResolverTest {

    private final CertificateEligibilityResolver resolver = new CertificateEligibilityResolver();

    @Test
    void eligibleWhenCourseCompletedAndExamPassed() {
        assertThat(resolver.isEligibleForCertificate(true, true)).isTrue();
    }

    @Test
    void notEligibleWhenCourseNotCompleted() {
        assertThat(resolver.isEligibleForCertificate(false, true)).isFalse();
    }

    @Test
    void notEligibleWhenExamNotPassed() {
        assertThat(resolver.isEligibleForCertificate(true, false)).isFalse();
    }

    @Test
    void notEligibleWhenNeitherConditionMet() {
        assertThat(resolver.isEligibleForCertificate(false, false)).isFalse();
    }
}

