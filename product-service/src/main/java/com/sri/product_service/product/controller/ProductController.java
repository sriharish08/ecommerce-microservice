package com.sri.product_service.product.controller;

import com.sri.product_service.common.response.Response;
import com.sri.product_service.product.dto.request.CreateProductRequest;
import com.sri.product_service.product.dto.request.DecrementStockRequest;
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

        Response<ProductResponse> response = Response.created();
        response.setPayload(service.create(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductResponse>> getProductById(
            @PathVariable("id") Long id) {

        Response<ProductResponse> response = Response.ok();
        response.setPayload(service.getById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response<List<ProductResponse>>> getProducts(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {

        System.out.println(userId);
        System.out.println(email);
        System.out.println(role);

        Response<List<ProductResponse>> response = Response.ok();
        response.setPayload(service.getAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Response<String>> test() {
        Response<String> response = Response.ok();
        response.setPayload("Products");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/stock/decrement")
    public ResponseEntity<Response<Void>> decrementStock(
            @PathVariable("id") Long id,
            @Valid @RequestBody DecrementStockRequest request) {

        service.decrementStock(id, request.getQuantity());
        Response<Void> response = Response.ok();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/stock/restore")
    public ResponseEntity<Response<Void>> restoreStock(
            @PathVariable("id") Long id,
            @Valid @RequestBody DecrementStockRequest request) {

        service.restoreStock(id, request.getQuantity());
        Response<Void> response = Response.ok();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
