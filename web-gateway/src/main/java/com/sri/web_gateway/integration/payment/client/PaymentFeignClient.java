package com.sri.web_gateway.integration.payment.client;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.integration.payment.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "payment-service",
        url = "${feign.client.payment-service}"
)
public interface PaymentFeignClient {

    @GetMapping("/{paymentId}")
    Response<PaymentResponse> getPayment(@PathVariable("paymentId") Long paymentId);

    @GetMapping("/order/{orderId}")
    Response<PaymentResponse> getByOrderId(@PathVariable("orderId") Long orderId);
}
