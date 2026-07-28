package com.sri.product_service.product.service.impl;

import com.sri.product_service.product.dto.request.CreateProductRequest;
import com.sri.product_service.product.dto.response.ProductResponse;
import com.sri.product_service.product.entity.Product;
import com.sri.product_service.product.exception.InsufficientStockException;
import com.sri.product_service.product.exception.ProductNotFoundException;
import com.sri.product_service.product.redis.RedisService;
import com.sri.product_service.product.service.mapper.ProductMapper;
import com.sri.product_service.product.repository.ProductRepository;
import com.sri.product_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final RedisService redisService;
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

        Product product = redisService.getProduct(id);

        if (product != null) {
            return mapper.toResponse(product);
        }

        product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        redisService.saveProduct(product);

        return mapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void decrementStock(Long id, Integer quantity) {

        int updated = repository.decrementStock(id, quantity);

        if (updated == 0) {

            if (!repository.existsById(id)) {
                throw new ProductNotFoundException("Product not found");
            }

            throw new InsufficientStockException(
                    "Insufficient stock for productId=" + id);
        }
    }

    @Override
    @Transactional
    public void restoreStock(Long id, Integer quantity) {

        int updated = repository.restoreStock(id, quantity);

        if (updated == 0) {
            throw new ProductNotFoundException("Product not found");
        }
    }

}