package com.sri.web_gateway.controller;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.common.response.ResponseBuilder;
import com.sri.web_gateway.integration.product.client.ProductClient;
import com.sri.web_gateway.integration.product.dto.CreateProductRequest;
import com.sri.web_gateway.integration.product.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductGatewayController {

    private final ProductClient productClient;

    @PostMapping
    public ResponseEntity<Response<ProductResponse>> create(@Valid @RequestBody CreateProductRequest request) {
        return ResponseBuilder.success(productClient.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductResponse>> getProductById(@PathVariable("id") Long id) {
        return ResponseBuilder.success(productClient.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Response<List<ProductResponse>>> getProducts() {
        return ResponseBuilder.success(productClient.getProducts());
    }

    @GetMapping("/test")
    public ResponseEntity<Response<String>> test() {
        return ResponseBuilder.success(productClient.test());
    }
}
