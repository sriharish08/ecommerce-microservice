package com.sri.order_service.integration.product.client;


import com.sri.order_service.common.response.Response;
import com.sri.order_service.integration.product.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service",
        url = "${feign.client.product-service}"
)
public interface ProductFeignClient {

    @GetMapping("/{id}")
    Response<ProductResponse> getProductById(@PathVariable("id") Long id);

}
