package com.sri.order_service.integration.product.client;

import com.sri.order_service.integration.product.dto.DecrementStockRequest;
import com.sri.order_service.integration.product.dto.ProductResponse;
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
public class ProductClient {

    private static final Logger log = LoggerFactory.getLogger(ProductClient.class);

    private final ProductFeignClient productFeignClient;

    @Retry(name = "getProductById")
    @CircuitBreaker(
            name = "getProductById",
            fallbackMethod = "getProductByIdFallback"
    )
    public ProductResponse getProductById(Long productId) {

        log.info("Calling Product Service for productId={}", productId);

        return productFeignClient.getProductById(productId).getPayload();
    }

    private ProductResponse getProductByIdFallback(Long productId, Throwable exception) {
        return fail(exception);
    }

    @CircuitBreaker(
            name = "decrementStock",
            fallbackMethod = "decrementStockFallback"
    )
    public void decrementStock(Long productId, Integer quantity) {

        log.info("Calling Product Service to decrement stock productId={}, quantity={}", productId, quantity);

        productFeignClient.decrementStock(productId, new DecrementStockRequest(quantity));
    }

    private void decrementStockFallback(Long productId, Integer quantity, Throwable exception) {
        fail(exception);
    }

    /**
     * Best-effort compensation, called after a payment has already failed - the order is
     * already being marked FAILED and that response is what the customer must see, so a
     * broken restore here is logged rather than thrown, leaving a stock discrepancy to be
     * reconciled instead of masking the real payment failure.
     */
    @CircuitBreaker(
            name = "restoreStock",
            fallbackMethod = "restoreStockFallback"
    )
    public void restoreStock(Long productId, Integer quantity) {

        log.info("Calling Product Service to restore stock productId={}, quantity={}", productId, quantity);

        productFeignClient.restoreStock(productId, new DecrementStockRequest(quantity));
    }

    private void restoreStockFallback(Long productId, Integer quantity, Throwable exception) {
        log.error("Failed to restore stock for productId={}, quantity={}. Reason: {}",
                productId, quantity, exception.getMessage(), exception);
    }

    /**
     * A 4xx from product-service (not found, insufficient stock, validation) is the caller's
     * fault, not a health signal for product-service - it's rethrown as-is instead of being
     * masked as "service unavailable" so the real status/reason reaches the caller.
     */
    private <T> T fail(Throwable exception) {

        if (exception instanceof FeignException.FeignClientException) {
            throw (FeignException) exception;
        }

        log.error("Product Service call failed. Reason: {}", exception.getMessage());

        throw new CustomException("Product Service is unavailable.");
    }
}
