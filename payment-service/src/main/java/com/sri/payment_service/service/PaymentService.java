package com.sri.payment_service.service;

import com.sri.payment_service.dto.request.PaymentRequest;
import com.sri.payment_service.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);

    PaymentResponse getPayment(Long paymentId, Long requesterId, String requesterRole);

    PaymentResponse getPaymentByOrderId(Long orderId, Long requesterId, String requesterRole);
}
