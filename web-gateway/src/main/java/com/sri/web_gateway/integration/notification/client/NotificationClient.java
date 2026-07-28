package com.sri.web_gateway.integration.notification.client;

import com.sri.web_gateway.exception.ServiceUnavailableException;
import com.sri.web_gateway.integration.notification.dto.NotificationHistoryResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);

    private final NotificationFeignClient notificationFeignClient;

    @Retry(name = "notificationGetById")
    @CircuitBreaker(name = "notificationGetById", fallbackMethod = "getByIdFallback")
    public NotificationHistoryResponse getById(Long id) {

        log.info("Calling Notification Service for notificationId={}", id);

        return notificationFeignClient.getById(id).getPayload();
    }

    private NotificationHistoryResponse getByIdFallback(Long id, Throwable exception) {
        return fail(exception);
    }

    @Retry(name = "notificationGetByUserId")
    @CircuitBreaker(name = "notificationGetByUserId", fallbackMethod = "getByUserIdFallback")
    public List<NotificationHistoryResponse> getByUserId(Long userId) {

        log.info("Calling Notification Service for userId={}", userId);

        return notificationFeignClient.getByUserId(userId).getPayload();
    }

    private List<NotificationHistoryResponse> getByUserIdFallback(Long userId, Throwable exception) {
        return fail(exception);
    }

    private <T> T fail(Throwable exception) {

        if (exception instanceof FeignException.FeignClientException) {
            throw (FeignException) exception;
        }

        log.error("Notification Service call failed. Reason: {}", exception.getMessage());

        throw new ServiceUnavailableException("notification-service", "notification-service is temporarily unavailable, please try again later");
    }
}
