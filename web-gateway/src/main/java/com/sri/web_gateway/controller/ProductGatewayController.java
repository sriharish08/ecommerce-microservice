package com.sri.web_gateway.controller;

import com.sri.web_gateway.common.response.Response;
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
        Response<ProductResponse> response = Response.created();
        response.setPayload(productClient.create(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductResponse>> getProductById(@PathVariable("id") Long id) {
        Response<ProductResponse> response = Response.ok();
        response.setPayload(productClient.getProductById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response<List<ProductResponse>>> getProducts() {
        Response<List<ProductResponse>> response = Response.ok();
        response.setPayload(productClient.getProducts());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Response<String>> test() {
        Response<String> response = Response.ok();
        response.setPayload(productClient.test());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
