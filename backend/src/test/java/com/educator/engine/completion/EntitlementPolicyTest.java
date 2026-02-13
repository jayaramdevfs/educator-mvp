package com.educator.engine.completion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntitlementPolicyTest {

    @Test
    void defaultsToNoEntitlements() {
        EntitlementPolicy policy = new EntitlementPolicy();

        assertThat(policy.hasCertificateAccess()).isFalse();
        assertThat(policy.hasLifetimeCourseAccess()).isFalse();
    }

    @Test
    void exposesSettableCertificateAccess() {
        EntitlementPolicy policy = new EntitlementPolicy();
        policy.setCertificateAccess(true);

        assertThat(policy.hasCertificateAccess()).isTrue();
    }

    @Test
    void exposesSettableLifetimeCourseAccess() {
        EntitlementPolicy policy = new EntitlementPolicy();
        policy.setLifetimeCourseAccess(true);

        assertThat(policy.hasLifetimeCourseAccess()).isTrue();
    }
}

