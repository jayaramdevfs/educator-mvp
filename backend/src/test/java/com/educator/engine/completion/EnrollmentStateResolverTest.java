package com.educator.engine.completion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnrollmentStateResolverTest {

    private final EnrollmentStateResolver resolver = new EnrollmentStateResolver();

    @Test
    void resolveReturnsCompletedWhenCompletedTrue() {
        assertThat(resolver.resolve(true))
                .isEqualTo(EnrollmentStateResolver.EnrollmentState.COMPLETED);
    }

    @Test
    void resolveReturnsActiveWhenCompletedFalse() {
        assertThat(resolver.resolve(false))
                .isEqualTo(EnrollmentStateResolver.EnrollmentState.ACTIVE);
    }
}

