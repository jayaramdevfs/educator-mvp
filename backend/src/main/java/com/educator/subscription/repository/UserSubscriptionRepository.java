package com.educator.subscription.repository;

import com.educator.subscription.entity.UserSubscription;
import com.educator.subscription.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, UUID> {

    Optional<UserSubscription> findByUserIdAndPlanIdAndStatus(Long userId, UUID planId, SubscriptionStatus status);

    List<UserSubscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    List<UserSubscription> findByStatusAndExpiresAtBefore(SubscriptionStatus status, LocalDateTime now);
}
