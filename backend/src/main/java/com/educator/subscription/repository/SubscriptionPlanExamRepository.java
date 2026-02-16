package com.educator.subscription.repository;

import com.educator.subscription.entity.SubscriptionPlanExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionPlanExamRepository extends JpaRepository<SubscriptionPlanExam, UUID> {

    List<SubscriptionPlanExam> findByPlanId(UUID planId);

    boolean existsByPlanIdAndExamId(UUID planId, UUID examId);

    void deleteByPlanIdAndExamId(UUID planId, UUID examId);
}
