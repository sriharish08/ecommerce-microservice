package com.sri.product_service.product.service.impl;

import com.sri.product_service.product.dto.request.CreateProductRequest;
import com.sri.product_service.product.dto.response.ProductResponse;
import com.sri.product_service.product.entity.Product;
import com.sri.product_service.product.exception.ProductNotFoundException;
import com.sri.product_service.product.service.mapper.ProductMapper;
import com.sri.product_service.product.repository.ProductRepository;
import com.sri.product_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public ProductResponse create(CreateProductRequest request) {

        Product product = mapper.toEntity(request);

        return mapper.toResponse(
                repository.save(product)
        );
    }

    @Override
    public ProductResponse getById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        return mapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}