package com.educator.payment.repository;

import com.educator.payment.entity.SubscriptionPayment;
import com.educator.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionPaymentRepository
        extends JpaRepository<SubscriptionPayment, UUID> {

    Optional<SubscriptionPayment> findByProviderPaymentId(String providerPaymentId);

    Optional<SubscriptionPayment> findByUserIdAndPlanIdAndStatus(
            Long userId,
            UUID planId,
            PaymentStatus status
    );
}
