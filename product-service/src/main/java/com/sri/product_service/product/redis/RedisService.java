package com.sri.product_service.product.redis;

import com.sri.product_service.product.entity.Product;

public interface RedisService {

     void saveProduct(Product product);

     Product getProduct(Long id);
}
