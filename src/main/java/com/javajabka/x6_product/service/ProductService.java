package com.javajabka.x6_product.service;

import com.javajabka.x6_product.exception.BadRequestException;
import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.model.ProductResponse;
import com.javajabka.x6_product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(rollbackFor = Exception.class)
    public ProductResponse createProduct(ProductRequest x6ProductRequest) {
        validate(x6ProductRequest);
        return productRepository.insert(x6ProductRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductResponse updateProduct(final Long id, final ProductRequest productRequest) {
        validate(productRequest);
        return productRepository.update(id, productRequest);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#id")
    public ProductResponse getProductById(final Long id) {
        return productRepository.getProductById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#id")
    public List<Long> exists(final List<Long> id) {
        return productRepository.exist(id);
    }

    private void validate(final ProductRequest productRequest) {
        if(productRequest == null) {
            throw new BadRequestException("Введите информацию о продукте");
        }
        if(!StringUtils.hasText(productRequest.getName())) {
            throw new BadRequestException("Введите имя продукта");
        }
        if(productRequest.getPrice() == null || productRequest.getPrice().equals(BigDecimal.ZERO)) {
            throw new BadRequestException("Введите корректную цену");
        }
    }
}
