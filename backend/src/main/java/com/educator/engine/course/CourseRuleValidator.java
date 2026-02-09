package com.educator.engine.course;

import java.util.List;
import java.util.Set;

/**
 * Validates a course structure against a CourseTemplate
 * and a set of CourseStructureRules.
 *
 * This class performs NO mutation.
 */
public class CourseRuleValidator {

    /**
     * Validates a course snapshot.
     *
     * @param template        the template defining required structure
     * @param actualModules   ordered set of module names present in the course
     * @param lessonsPerModule count of lessons per module (aligned with module order)
     * @return validation result with violations, if any
     */
    public CourseValidationResult validate(
            CourseTemplate template,
            List<String> actualModules,
            List<Integer> lessonsPerModule
    ) {
        CourseValidationResult result = new CourseValidationResult();

        // Rule: Required modules must exist (in order, if enforced)
        validateRequiredModules(template, actualModules, result);

        // Rule: Minimum lessons per module
        validateLessonCounts(template, lessonsPerModule, result);

        return result;
    }

    /* -------------------- Rule Implementations -------------------- */

    private void validateRequiredModules(
            CourseTemplate template,
            List<String> actualModules,
            CourseValidationResult result
    ) {
        List<String> required = template.getRequiredModules();
        if (required == null || required.isEmpty()) {
            return;
        }

        if (template.isEnforceOrdering()) {
            int lastIndex = -1;
            for (String requiredModule : required) {
                int index = actualModules.indexOf(requiredModule);
                if (index == -1 || index < lastIndex) {
                    result.addViolation(
                            new CourseValidationResult.Violation(
                                    "MODULE_ORDER_INVALID",
                                    "Required module missing or out of order: " + requiredModule,
                                    CourseStructureRule.RuleSeverity.ERROR,
                                    true
                            )
                    );
                    return;
                }
                lastIndex = index;
            }
        } else {
            for (String requiredModule : required) {
                if (!actualModules.contains(requiredModule)) {
                    result.addViolation(
                            new CourseValidationResult.Violation(
                                    "MODULE_MISSING",
                                    "Required module missing: " + requiredModule,
                                    CourseStructureRule.RuleSeverity.ERROR,
                                    true
                            )
                    );
                }
            }
        }
    }

    private void validateLessonCounts(
            CourseTemplate template,
            List<Integer> lessonsPerModule,
            CourseValidationResult result
    ) {
        int minLessons = template.getMinLessonsPerModule();
        if (minLessons <= 0) {
            return;
        }

        for (int i = 0; i < lessonsPerModule.size(); i++) {
            if (lessonsPerModule.get(i) < minLessons) {
                result.addViolation(
                        new CourseValidationResult.Violation(
                                "LESSON_COUNT_TOO_LOW",
                                "Module at position " + (i + 1) +
                                        " has fewer than " + minLessons + " lessons",
                                CourseStructureRule.RuleSeverity.WARNING,
                                false
                        )
                );
            }
        }
    }
}
