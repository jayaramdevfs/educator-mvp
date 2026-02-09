package com.educator.engine.completion;

import java.util.Objects;

public class CompletionRuleEngine {

    /**
     * Determines if a course is completed.
     *
     * @param lessonsCompleted total lessons completed
     * @param totalLessons total lessons in course
     * @param examPassed whether the exam was passed
     */
    public boolean isCourseCompleted(
            int lessonsCompleted,
            int totalLessons,
            boolean examPassed
    ) {
        if (totalLessons <= 0) {
            return false;
        }

        boolean lessonsSatisfied = lessonsCompleted >= totalLessons;
        return lessonsSatisfied && examPassed;
    }
}
