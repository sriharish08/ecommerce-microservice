package com.sri.web_gateway.integration.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer quantity;

}
