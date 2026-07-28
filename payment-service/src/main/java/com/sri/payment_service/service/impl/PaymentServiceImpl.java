package com.sri.payment_service.service.impl;


import com.sri.payment_service.dto.request.PaymentRequest;
import com.sri.payment_service.dto.response.PaymentResponse;
import com.sri.payment_service.entity.Payment;
import com.sri.payment_service.enums.PaymentMethod;
import com.sri.payment_service.enums.PaymentStatus;
import com.sri.payment_service.exception.PaymentAccessDeniedException;
import com.sri.payment_service.exception.ResourceNotFoundException;
import com.sri.payment_service.repository.PaymentRepository;
import com.sri.payment_service.service.PaymentService;
import com.sri.payment_service.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {

        if (paymentRepository.existsByOrderId(request.getOrderId())) {
            throw new IllegalArgumentException(
                    "Payment already exists for Order ID : " + request.getOrderId());
        }

        // Cash on Delivery collects nothing up front - PENDING until collected on delivery,
        // rather than falsely marking it SUCCESS before any money has changed hands.
        PaymentStatus status = request.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY
                ? PaymentStatus.PENDING
                : PaymentStatus.SUCCESS;

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(status)
                .transactionId(UUID.randomUUID().toString())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }

    private static final String ADMIN_ROLE = "ADMIN";

    @Override
    public PaymentResponse getPayment(Long paymentId, Long requesterId, String requesterRole) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id : " + paymentId));

        assertOwnerOrAdmin(payment, requesterId, requesterRole);

        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId, Long requesterId, String requesterRole) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id : " + orderId));

        assertOwnerOrAdmin(payment, requesterId, requesterRole);

        return paymentMapper.toResponse(payment);
    }

    private void assertOwnerOrAdmin(Payment payment, Long requesterId, String requesterRole) {

        if (ADMIN_ROLE.equals(requesterRole)) {
            return;
        }

        if (!payment.getUserId().equals(requesterId)) {
            throw new PaymentAccessDeniedException("You do not have access to this payment");
        }
    }
}