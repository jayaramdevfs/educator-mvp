package com.educator.exam.enums;

/**
 * Defines the lifecycle state of an Exam Attempt.
 *
 * IN_PROGRESS : Learner has started the exam but has not yet submitted.
 * SUBMITTED   : Learner has submitted the attempt.
 * EVALUATED   : Attempt has been evaluated and result is finalized.
 * EXPIRED     : Attempt expired due to time constraints (foundation only).
 *
 * This enum is intentionally minimal and deterministic.
 */
public enum AttemptStatus {

    IN_PROGRESS,
    SUBMITTED,
    EVALUATED,
    EXPIRED

}
