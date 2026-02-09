package com.educator.engine.completion;

public class EnrollmentStateResolver {

    public enum EnrollmentState {
        ACTIVE,
        COMPLETED
    }

    public EnrollmentState resolve(boolean completed) {
        return completed ? EnrollmentState.COMPLETED : EnrollmentState.ACTIVE;
    }
}
