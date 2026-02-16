package com.educator.subscription.controller;

import com.educator.payment.provider.PaymentInitiationResponse;
import com.educator.payment.service.PaymentService;
import com.educator.security.CustomUserDetails;
import com.educator.subscription.dto.UserSubscriptionResponse;
import com.educator.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learner/subscriptions")
@RequiredArgsConstructor
public class LearnerSubscriptionController {

    private final PaymentService paymentService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/{planId}/buy")
    public PaymentInitiationResponse initiatePurchase(
            @PathVariable UUID planId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return paymentService.initiatePayment(
                userDetails.getUser().getId(),
                planId
        );
    }

    @GetMapping("/my")
    public List<UserSubscriptionResponse> mySubscriptions(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return subscriptionService.getUserSubscriptions(
                userDetails.getUser().getId()
        );
    }
}
