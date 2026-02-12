package com.educator.completion.repository;

import com.educator.completion.entity.CourseCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CourseCompletionRepository extends JpaRepository<CourseCompletion, UUID> {

    Optional<CourseCompletion> findByCourseIdAndUserId(Long courseId, UUID userId);

}
