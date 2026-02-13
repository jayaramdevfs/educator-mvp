package com.educator.exam.repository;

import com.educator.exam.entity.Exam;
import com.educator.exam.enums.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Exam entity.
 */
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    /**
     * Find exam by course ID.
     */
    Optional<Exam> findByCourseId(Long courseId);

    /**
     * Check if an exam exists for a given course and status.
     */
    boolean existsByCourseIdAndStatus(Long courseId, ExamStatus status);

    long countByStatus(ExamStatus status);
}
