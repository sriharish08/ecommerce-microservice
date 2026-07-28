package com.sri.order_service.integration.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecrementStockRequest {

    private Integer quantity;
}
