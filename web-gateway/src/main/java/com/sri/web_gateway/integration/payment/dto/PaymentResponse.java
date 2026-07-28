package com.sri.web_gateway.integration.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long paymentId;

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private String paymentMethod;

    private String paymentStatus;

    private String transactionId;

    private LocalDateTime createdAt;
}
