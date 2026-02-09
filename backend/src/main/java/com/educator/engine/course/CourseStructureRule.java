package com.educator.engine.course;

/**
 * Represents a single structural rule that can be applied
 * to validate a Course.
 *
 * This rule is purely declarative and does not execute logic itself.
 */
public class CourseStructureRule {

    /**
     * Unique rule identifier.
     * Example: "REQUIRES_ASSESSMENT_MODULE"
     */
    private String ruleCode;

    /**
     * Human-readable description of the rule.
     */
    private String description;

    /**
     * Whether violation of this rule should block publishing.
     */
    private boolean blocking;

    /**
     * Optional severity level (INFO, WARNING, ERROR).
     */
    private RuleSeverity severity;

    public enum RuleSeverity {
        INFO,
        WARNING,
        ERROR
    }

    /* -------------------- Getters & Setters -------------------- */

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public RuleSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(RuleSeverity severity) {
        this.severity = severity;
    }
}
