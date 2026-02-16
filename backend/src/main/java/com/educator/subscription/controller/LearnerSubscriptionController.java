package com.educator.subscription.controller;

import com.educator.payment.provider.PaymentInitiationResponse;
import com.educator.payment.service.PaymentService;
import com.educator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/learner/subscriptions")
@RequiredArgsConstructor
public class LearnerSubscriptionController {

    private final PaymentService paymentService;

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
}
