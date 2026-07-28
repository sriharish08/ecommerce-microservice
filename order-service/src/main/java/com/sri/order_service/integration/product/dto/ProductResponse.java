package com.sri.order_service.integration.product.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer quantity;
}