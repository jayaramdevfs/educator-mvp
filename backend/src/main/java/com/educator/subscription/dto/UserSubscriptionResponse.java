package com.educator.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserSubscriptionResponse {

    private UUID subscriptionId;
    private UUID planId;
    private String status;
    private LocalDateTime startAt;
    private LocalDateTime expiresAt;
    private long remainingDays;

    public static UserSubscriptionResponse of(
            UUID subscriptionId,
            UUID planId,
            String status,
            LocalDateTime startAt,
            LocalDateTime expiresAt
    ) {
        long remaining = ChronoUnit.DAYS.between(
                LocalDateTime.now(),
                expiresAt
        );

        if (remaining < 0) {
            remaining = 0;
        }

        return UserSubscriptionResponse.builder()
                .subscriptionId(subscriptionId)
                .planId(planId)
                .status(status)
                .startAt(startAt)
                .expiresAt(expiresAt)
                .remainingDays(remaining)
                .build();
    }
}
