package com.sri.order_service.integration.payment.client;

import com.sri.order_service.common.response.Response;
import com.sri.order_service.integration.payment.dto.PaymentRequest;
import com.sri.order_service.integration.payment.dto.PaymentResponse;
import com.sri.order_service.order.exception.CustomException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentClient.class);

    private final PaymentFeignClient paymentFeignClient;

    @Retry(name = "processPayment")
    @CircuitBreaker(
            name = "processPayment",
            fallbackMethod = "processPaymentFallback"
    )
    public Response<PaymentResponse> processPayment(PaymentRequest request) {

        log.info("Calling Payment Service for orderId={}", request.getOrderId());

        return paymentFeignClient.processPayment(request);
    }

    private Response<PaymentResponse> processPaymentFallback(PaymentRequest request,
                                                   Throwable exception) {

        if (exception instanceof FeignException.FeignClientException) {
            throw (FeignException) exception;
        }

        log.error("Failed to process payment for orderId={}. Reason: {}",
                request.getOrderId(),
                exception.getMessage());

        throw new CustomException("Payment Service is unavailable.");
    }
}
