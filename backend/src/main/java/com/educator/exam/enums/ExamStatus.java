package com.educator.exam.enums;

/**
 * Defines the lifecycle state of an Exam.
 *
 * DRAFT     : Exam is being created or edited and is not visible to learners.
 * PUBLISHED : Exam is active and can be attempted by learners.
 * ARCHIVED  : Exam is no longer active and cannot be attempted.
 *
 * This enum is intentionally minimal and deterministic.
 */
public enum ExamStatus {

    DRAFT,
    PUBLISHED,
    ARCHIVED

}
