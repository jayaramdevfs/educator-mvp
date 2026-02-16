package com.educator.payment.service;

import com.educator.payment.entity.SubscriptionPayment;
import com.educator.payment.enums.PaymentStatus;
import com.educator.payment.provider.PaymentInitiationResponse;
import com.educator.payment.provider.PaymentProvider;
import com.educator.payment.repository.SubscriptionPaymentRepository;
import com.educator.subscription.entity.SubscriptionPlan;
import com.educator.subscription.repository.SubscriptionPlanRepository;
import com.educator.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProvider paymentProvider;
    private final SubscriptionPaymentRepository paymentRepository;
    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionService subscriptionService;

    @Transactional
    public PaymentInitiationResponse initiatePayment(Long userId, UUID planId) {

        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        PaymentInitiationResponse response =
                paymentProvider.initiatePayment(
                        userId,
                        planId,
                        plan.getPrice()
                );

        SubscriptionPayment payment = SubscriptionPayment.builder()
                .userId(userId)
                .planId(planId)
                .amount(plan.getPrice())
                .status(PaymentStatus.PENDING)
                .provider(response.getProvider())
                .providerPaymentId(response.getProviderPaymentId())
                .build();

        paymentRepository.save(payment);

        return response;
    }

    @Transactional
    public void confirmPayment(String providerPaymentId, String signature) {

        SubscriptionPayment payment = paymentRepository
                .findByProviderPaymentId(providerPaymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return; // idempotent safety
        }

        boolean verified =
                paymentProvider.verifyPayment(providerPaymentId, signature);

        if (!verified) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            return;
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // 🔐 Activate subscription only after successful payment
        subscriptionService.activateSubscription(

                payment.getUserId(),
                payment.getPlanId()
        );
    }
}
