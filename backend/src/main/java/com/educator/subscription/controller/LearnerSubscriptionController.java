package com.educator.subscription.controller;

import com.educator.security.CustomUserDetails;
import com.educator.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/learner/subscriptions")
@RequiredArgsConstructor
public class LearnerSubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{planId}/buy")
    public void buySubscription(@PathVariable UUID planId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        subscriptionService.purchaseSubscription(
                userDetails.getUser().getId(),
                planId
        );
    }
}
