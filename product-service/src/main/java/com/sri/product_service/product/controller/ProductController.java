package com.sri.product_service.product.controller;

import com.sri.product_service.common.response.Response;
import com.sri.product_service.common.response.ResponseBuilder;
import com.sri.product_service.product.dto.request.CreateProductRequest;
import com.sri.product_service.product.dto.response.ProductResponse;
import com.sri.product_service.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<Response<ProductResponse>> create(
            @Valid @RequestBody CreateProductRequest request) {

        return ResponseBuilder.success(service.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductResponse>> getProductById(
            @PathVariable("id") Long id) {

        return ResponseBuilder.success(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Response<List<ProductResponse>>> getProducts(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {

        System.out.println(userId);
        System.out.println(email);
        System.out.println(role);

        return ResponseBuilder.success(service.getAll());
    }

    @GetMapping("/test")
    public ResponseEntity<Response<String>> test() {
        return ResponseBuilder.success("Products");
    }
}
