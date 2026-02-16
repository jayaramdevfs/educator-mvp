package com.educator.subscription.service;

import com.educator.subscription.dto.UserSubscriptionResponse;
import com.educator.subscription.entity.SubscriptionPlan;
import com.educator.subscription.entity.SubscriptionStatus;
import com.educator.subscription.entity.UserSubscription;
import com.educator.subscription.repository.SubscriptionPlanRepository;
import com.educator.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    /**
     * Activate subscription after successful payment
     */
    @Transactional
    public void activateSubscription(Long userId, UUID planId) {

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusDays(plan.getDurationDays());

        UserSubscription subscription = UserSubscription.builder()
                .userId(userId)
                .planId(planId)
                .startAt(now)
                .expiresAt(expiry)
                .status(SubscriptionStatus.ACTIVE)
                .build();

        userSubscriptionRepository.save(subscription);
    }

    /**
     * Get active subscriptions for logged-in user
     */
    public List<UserSubscriptionResponse> getUserSubscriptions(Long userId) {

        List<UserSubscription> subscriptions =
                userSubscriptionRepository.findByUserIdAndStatus(
                        userId,
                        SubscriptionStatus.ACTIVE
                );

        return subscriptions.stream()
                .map(sub -> UserSubscriptionResponse.of(
                        sub.getId(),
                        sub.getPlanId(),
                        sub.getStatus().name(),
                        sub.getStartAt(),
                        sub.getExpiresAt()
                ))
                .toList();
    }

    /**
     * Expire subscriptions if needed
     */
    @Transactional
    public void expireSubscriptionsIfNeeded() {

        List<UserSubscription> expired =
                userSubscriptionRepository.findByStatusAndExpiresAtBefore(
                        SubscriptionStatus.ACTIVE,
                        LocalDateTime.now()
                );

        for (UserSubscription sub : expired) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
        }

        userSubscriptionRepository.saveAll(expired);
    }
}
