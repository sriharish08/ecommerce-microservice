package com.sri.web_gateway.integration.product.client;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.integration.product.dto.CreateProductRequest;
import com.sri.web_gateway.integration.product.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${feign.client.product-service}"
)
public interface ProductFeignClient {

    @PostMapping
    Response<ProductResponse> create(@RequestBody CreateProductRequest request);

    @GetMapping("/{id}")
    Response<ProductResponse> getProductById(@PathVariable("id") Long id);

    @GetMapping
    Response<List<ProductResponse>> getProducts();

    @GetMapping("/test")
    Response<String> test();
}
