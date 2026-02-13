package com.educator.engine.completion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompletionRuleEngineTest {

    private final CompletionRuleEngine engine = new CompletionRuleEngine();

    @Test
    void courseCompletedWhenLessonsMetAndExamPassed() {
        assertThat(engine.isCourseCompleted(10, 10, true)).isTrue();
    }

    @Test
    void courseCompletedWhenLessonsExceededAndExamPassed() {
        assertThat(engine.isCourseCompleted(12, 10, true)).isTrue();
    }

    @Test
    void courseNotCompletedWhenLessonsBelowTotal() {
        assertThat(engine.isCourseCompleted(9, 10, true)).isFalse();
    }

    @Test
    void courseNotCompletedWhenExamNotPassed() {
        assertThat(engine.isCourseCompleted(10, 10, false)).isFalse();
    }

    @Test
    void courseNotCompletedWhenTotalLessonsIsZero() {
        assertThat(engine.isCourseCompleted(0, 0, true)).isFalse();
    }

    @Test
    void courseNotCompletedWhenTotalLessonsNegative() {
        assertThat(engine.isCourseCompleted(5, -1, true)).isFalse();
    }
}

