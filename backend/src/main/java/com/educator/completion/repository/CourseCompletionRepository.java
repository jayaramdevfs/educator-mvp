package com.educator.completion.repository;

import com.educator.completion.entity.CourseCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for CourseCompletion entity.
 *
 * Contains persistence-level queries only.
 */
public interface CourseCompletionRepository extends JpaRepository<CourseCompletion, UUID> {

    /**
     * Find completion record for a given user and course.
     */
    Optional<CourseCompletion> findByCourseIdAndUserId(UUID courseId, UUID userId);

    /**
     * Check whether a course is completed by a user.
     */
    boolean existsByCourseIdAndUserId(UUID courseId, UUID userId);
}
