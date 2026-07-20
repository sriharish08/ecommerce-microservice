package com.sri.web_gateway.integration.product.client;

import com.sri.web_gateway.exception.ServiceUnavailableException;
import com.sri.web_gateway.integration.product.dto.CreateProductRequest;
import com.sri.web_gateway.integration.product.dto.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {

    private static final Logger log = LoggerFactory.getLogger(ProductClient.class);

    private final ProductFeignClient productFeignClient;

    @CircuitBreaker(name = "productCreate", fallbackMethod = "createFallback")
    public ProductResponse create(CreateProductRequest request) {

        log.info("Calling Product Service to create product name={}", request.getName());

        return productFeignClient.create(request).getPayload();
    }

    private ProductResponse createFallback(CreateProductRequest request, Throwable exception) {
        return fail(exception);
    }

    @Retry(name = "productGetById")
    @CircuitBreaker(name = "productGetById", fallbackMethod = "getProductByIdFallback")
    public ProductResponse getProductById(Long id) {

        log.info("Calling Product Service for productId={}", id);

        return productFeignClient.getProductById(id).getPayload();
    }

    private ProductResponse getProductByIdFallback(Long id, Throwable exception) {
        return fail(exception);
    }

    @Retry(name = "productGetAll")
    @CircuitBreaker(name = "productGetAll", fallbackMethod = "getProductsFallback")
    public List<ProductResponse> getProducts() {

        log.info("Calling Product Service to fetch all products");

        return productFeignClient.getProducts().getPayload();
    }

    private List<ProductResponse> getProductsFallback(Throwable exception) {
        return fail(exception);
    }

    @Retry(name = "productTest")
    @CircuitBreaker(name = "productTest", fallbackMethod = "testFallback")
    public String test() {
        return productFeignClient.test().getPayload();
    }

    private String testFallback(Throwable exception) {
        return fail(exception);
    }

    private <T> T fail(Throwable exception) {

        log.error("Product Service call failed. Reason: {}", exception.getMessage());

        throw new ServiceUnavailableException("product-service", "product-service is temporarily unavailable, please try again later");
    }
}
