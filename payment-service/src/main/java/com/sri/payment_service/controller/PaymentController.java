package com.sri.payment_service.controller;


import com.sri.payment_service.common.response.Response;
import com.sri.payment_service.dto.request.PaymentRequest;
import com.sri.payment_service.dto.response.PaymentResponse;
import com.sri.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Response<PaymentResponse>> processPayment(
            @Valid @RequestBody PaymentRequest request) {

        Response<PaymentResponse> response = Response.ok();
        response.setPayload(paymentService.processPayment(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Response<PaymentResponse>> getPayment(
            @PathVariable Long paymentId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Response<PaymentResponse> response = Response.ok();
        response.setPayload(paymentService.getPayment(paymentId, userId, role));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Response<PaymentResponse>> getByOrderId(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Response<PaymentResponse> response = Response.ok();
        response.setPayload(paymentService.getPaymentByOrderId(orderId, userId, role));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
