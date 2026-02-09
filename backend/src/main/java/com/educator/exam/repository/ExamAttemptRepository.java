package com.educator.exam.repository;

import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.enums.AttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ExamAttempt entity.
 *
 * Contains persistence-level queries only.
 */
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, UUID> {

    /**
     * Fetch all attempts for a user and exam.
     */
    List<ExamAttempt> findByExamIdAndUserId(UUID examId, UUID userId);

    /**
     * Count attempts for a user and exam.
     */
    long countByExamIdAndUserId(UUID examId, UUID userId);

    /**
     * Find the latest evaluated attempt for a user and exam.
     */
    Optional<ExamAttempt> findTopByExamIdAndUserIdAndStatusOrderByEvaluatedAtDesc(
            UUID examId,
            UUID userId,
            AttemptStatus status
    );
}
