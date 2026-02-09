package com.educator.engine.completion;

public class CertificateEligibilityResolver {

    public boolean isEligibleForCertificate(
            boolean courseCompleted,
            boolean examPassed
    ) {
        return courseCompleted && examPassed;
    }
}
