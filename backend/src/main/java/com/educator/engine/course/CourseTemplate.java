package com.educator.engine.course;

import java.util.List;
import java.util.UUID;

/**
 * Defines a structural template for a Course.
 *
 * This template does NOT contain content.
 * It defines how a course should be structured.
 */
public class CourseTemplate {

    private UUID id;

    /**
     * Template name (e.g., "History Course", "Certification Course").
     */
    private String name;

    /**
     * Required module names in order.
     * Example: Introduction, Core Concepts, Assessment, Conclusion
     */
    private List<String> requiredModules;

    /**
     * Whether an assessment module is mandatory.
     */
    private boolean assessmentRequired;

    /**
     * Minimum number of lessons per module.
     */
    private int minLessonsPerModule;

    /**
     * Whether course sequencing must be strictly enforced.
     */
    private boolean enforceOrdering;

    /**
     * Whether the course can be published if validation fails.
     */
    private boolean allowPublishOnValidationFailure = false;

    /* -------------------- Getters & Setters -------------------- */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRequiredModules() {
        return requiredModules;
    }

    public void setRequiredModules(List<String> requiredModules) {
        this.requiredModules = requiredModules;
    }

    public boolean isAssessmentRequired() {
        return assessmentRequired;
    }

    public void setAssessmentRequired(boolean assessmentRequired) {
        this.assessmentRequired = assessmentRequired;
    }

    public int getMinLessonsPerModule() {
        return minLessonsPerModule;
    }

    public void setMinLessonsPerModule(int minLessonsPerModule) {
        this.minLessonsPerModule = minLessonsPerModule;
    }

    public boolean isEnforceOrdering() {
        return enforceOrdering;
    }

    public void setEnforceOrdering(boolean enforceOrdering) {
        this.enforceOrdering = enforceOrdering;
    }

    public boolean isAllowPublishOnValidationFailure() {
        return allowPublishOnValidationFailure;
    }

    public void setAllowPublishOnValidationFailure(boolean allowPublishOnValidationFailure) {
        this.allowPublishOnValidationFailure = allowPublishOnValidationFailure;
    }
}
