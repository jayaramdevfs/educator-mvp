package com.educator.engine.course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the result of validating a Course
 * against one or more CourseStructureRules.
 */
public class CourseValidationResult {

    private final List<Violation> violations = new ArrayList<>();

    /**
     * Represents a single rule violation.
     */
    public static class Violation {

        private final String ruleCode;
        private final String message;
        private final CourseStructureRule.RuleSeverity severity;
        private final boolean blocking;

        public Violation(
                String ruleCode,
                String message,
                CourseStructureRule.RuleSeverity severity,
                boolean blocking
        ) {
            this.ruleCode = ruleCode;
            this.message = message;
            this.severity = severity;
            this.blocking = blocking;
        }

        public String getRuleCode() {
            return ruleCode;
        }

        public String getMessage() {
            return message;
        }

        public CourseStructureRule.RuleSeverity getSeverity() {
            return severity;
        }

        public boolean isBlocking() {
            return blocking;
        }
    }

    /* -------------------- Result Helpers -------------------- */

    public void addViolation(Violation violation) {
        this.violations.add(violation);
    }

    public List<Violation> getViolations() {
        return Collections.unmodifiableList(violations);
    }

    /**
     * Whether publishing should be blocked.
     */
    public boolean hasBlockingErrors() {
        return violations.stream().anyMatch(Violation::isBlocking);
    }

    /**
     * Whether the course is valid for publishing.
     */
    public boolean isPublishable() {
        return !hasBlockingErrors();
    }
}
