package com.sri.order_service.integration.payment.client;


import com.sri.order_service.common.response.Response;
import com.sri.order_service.integration.payment.dto.PaymentRequest;
import com.sri.order_service.integration.payment.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${feign.client.payment-service}"
)
public interface PaymentFeignClient {

    @PostMapping
    Response<PaymentResponse> processPayment(@RequestBody PaymentRequest request);

}