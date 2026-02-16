package com.educator.payment.controller;

import com.educator.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentService paymentService;

    @PostMapping
    public void confirmPayment(
            @RequestParam String providerPaymentId,
            @RequestParam(required = false) String signature
    ) {
        paymentService.confirmPayment(providerPaymentId, signature);
    }
}
