package com.educator.payment.provider;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class MockPaymentProvider implements PaymentProvider {

    @Override
    public PaymentInitiationResponse initiatePayment(
            Long userId,
            UUID planId,
            BigDecimal amount
    ) {

        String mockPaymentId = "MOCK-" + UUID.randomUUID();

        return new PaymentInitiationResponse(
                "MOCK",
                mockPaymentId,
                "https://mock-payment-gateway/success"
        );
    }

    @Override
    public boolean verifyPayment(String providerPaymentId, String signature) {

        // For mock provider, always success
        return providerPaymentId != null && providerPaymentId.startsWith("MOCK-");
    }
}
