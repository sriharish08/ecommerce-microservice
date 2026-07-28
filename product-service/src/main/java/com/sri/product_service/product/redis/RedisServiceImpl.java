package com.sri.product_service.product.redis;

import com.sri.product_service.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private static final String PRODUCT_CACHE = "PRODUCT";

    private final RedisRepository redisRepository;

    @Override
    public void saveProduct(Product product) {

        redisRepository.getHashOperations()
                .put(PRODUCT_CACHE, product.getId().toString(), product);
    }

    @Override
    public Product getProduct(Long id) {

        Object object = redisRepository.getHashOperations()
                .get(PRODUCT_CACHE, id.toString());

        return object != null ? (Product) object : null;
    }
}
