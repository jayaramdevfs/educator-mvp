package com.educator.engine.completion;

public class EntitlementPolicy {

    private boolean certificateAccess;
    private boolean lifetimeCourseAccess;

    public boolean hasCertificateAccess() {
        return certificateAccess;
    }

    public void setCertificateAccess(boolean certificateAccess) {
        this.certificateAccess = certificateAccess;
    }

    public boolean hasLifetimeCourseAccess() {
        return lifetimeCourseAccess;
    }

    public void setLifetimeCourseAccess(boolean lifetimeCourseAccess) {
        this.lifetimeCourseAccess = lifetimeCourseAccess;
    }
}
