package com.sri.order_service.integration.product.client;


import com.sri.order_service.common.response.Response;
import com.sri.order_service.integration.product.dto.DecrementStockRequest;
import com.sri.order_service.integration.product.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        url = "${feign.client.product-service}"
)
public interface ProductFeignClient {

    @GetMapping("/{id}")
    Response<ProductResponse> getProductById(@PathVariable("id") Long id);

    @PatchMapping("/{id}/stock/decrement")
    Response<Void> decrementStock(@PathVariable("id") Long id, @RequestBody DecrementStockRequest request);

    @PatchMapping("/{id}/stock/restore")
    Response<Void> restoreStock(@PathVariable("id") Long id, @RequestBody DecrementStockRequest request);

}
