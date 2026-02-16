package com.educator.payment.provider;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentProvider {

    PaymentInitiationResponse initiatePayment(
            Long userId,
            UUID planId,
            BigDecimal amount
    );

    boolean verifyPayment(String providerPaymentId, String signature);
}
