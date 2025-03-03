package com.javajabka.x6_product.service;

import com.javajabka.x6_product.exception.BadRequestException;
import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.model.ProductResponse;
import com.javajabka.x6_product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest x6ProductRequest) {
        validate(x6ProductRequest);
        return productRepository.createProduct(x6ProductRequest);
    }

    public ProductResponse updateProduct(final Long productId, final ProductRequest x6ProductRequest) {
        validate(x6ProductRequest);
        return productRepository.updateProduct(productId, x6ProductRequest);
    }

    public ProductResponse getUserById(final Long id) {
        return productRepository.getProductById(id);
    }

    public List<Long> exists(final List<Long> listId) {
        return productRepository.existsProduct(listId);
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
