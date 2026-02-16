package com.educator.subscription.repository;

import com.educator.subscription.entity.SubscriptionPlanCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionPlanCourseRepository extends JpaRepository<SubscriptionPlanCourse, UUID> {

    List<SubscriptionPlanCourse> findByPlanId(UUID planId);

    boolean existsByPlanIdAndCourseId(UUID planId, Long courseId);

    void deleteByPlanIdAndCourseId(UUID planId, Long courseId);
}
