package com.sri.order_service.integration.product.client;

import com.sri.order_service.integration.product.dto.ProductResponse;
import com.sri.order_service.order.exception.CustomException;
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

        log.error("Failed to fetch product {}. Reason: {}",
                productId,
                exception.getMessage());

        throw new CustomException("Product Service is unavailable.");
    }
}
