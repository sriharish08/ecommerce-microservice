package com.sri.web_gateway.controller;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.integration.payment.client.PaymentClient;
import com.sri.web_gateway.integration.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentGatewayController {

    private final PaymentClient paymentClient;

    @GetMapping("/{paymentId}")
    public ResponseEntity<Response<PaymentResponse>> getPayment(@PathVariable("paymentId") Long paymentId) {
        Response<PaymentResponse> response = Response.ok();
        response.setPayload(paymentClient.getPayment(paymentId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Response<PaymentResponse>> getByOrderId(@PathVariable("orderId") Long orderId) {
        Response<PaymentResponse> response = Response.ok();
        response.setPayload(paymentClient.getByOrderId(orderId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
