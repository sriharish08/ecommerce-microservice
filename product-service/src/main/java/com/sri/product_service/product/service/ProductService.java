package com.sri.product_service.product.service;


import com.sri.product_service.product.dto.request.CreateProductRequest;
import com.sri.product_service.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

}