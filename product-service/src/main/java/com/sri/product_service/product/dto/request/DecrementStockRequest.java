package com.sri.product_service.product.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DecrementStockRequest {

    @NotNull
    @Positive
    private Integer quantity;

}
