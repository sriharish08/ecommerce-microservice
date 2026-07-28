package com.sri.web_gateway.integration.payment.client;

import com.sri.web_gateway.exception.ServiceUnavailableException;
import com.sri.web_gateway.integration.payment.dto.PaymentResponse;
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

    @Retry(name = "paymentGetById")
    @CircuitBreaker(name = "paymentGetById", fallbackMethod = "getPaymentFallback")
    public PaymentResponse getPayment(Long paymentId) {

        log.info("Calling Payment Service for paymentId={}", paymentId);

        return paymentFeignClient.getPayment(paymentId).getPayload();
    }

    private PaymentResponse getPaymentFallback(Long paymentId, Throwable exception) {
        return fail(exception);
    }

    @Retry(name = "paymentGetByOrderId")
    @CircuitBreaker(name = "paymentGetByOrderId", fallbackMethod = "getByOrderIdFallback")
    public PaymentResponse getByOrderId(Long orderId) {

        log.info("Calling Payment Service for orderId={}", orderId);

        return paymentFeignClient.getByOrderId(orderId).getPayload();
    }

    private PaymentResponse getByOrderIdFallback(Long orderId, Throwable exception) {
        return fail(exception);
    }

    /**
     * A 4xx from payment-service (not found, access denied) is the caller's fault, not a
     * health signal for payment-service - it's rethrown as-is instead of being masked as 503.
     */
    private <T> T fail(Throwable exception) {

        if (exception instanceof FeignException.FeignClientException) {
            throw (FeignException) exception;
        }

        log.error("Payment Service call failed. Reason: {}", exception.getMessage());

        throw new ServiceUnavailableException("payment-service", "payment-service is temporarily unavailable, please try again later");
    }
}
