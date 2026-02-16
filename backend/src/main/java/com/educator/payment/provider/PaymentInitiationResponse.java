package com.educator.payment.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentInitiationResponse {

    private String provider;
    private String providerPaymentId;
    private String redirectUrl;
}
