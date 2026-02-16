package com.educator.payment.dto;

import com.educator.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PaymentHistoryResponse {

    private UUID paymentId;

    private UUID planId;

    private BigDecimal amount;

    private PaymentStatus status;

    private String provider;

    private String providerPaymentId;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}
