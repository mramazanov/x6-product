package com.javajabka.x6_product.service;

import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.model.ProductResponse;
import com.javajabka.x6_product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest x6ProductRequest) {
        return productRepository.createProduct(x6ProductRequest);
    }

    public ProductResponse updateProduct(final Long productId, final ProductRequest x6ProductRequest) {
        return productRepository.updateProduct(productId, x6ProductRequest);
    }

    public ProductResponse getUserById(final Long id) {
        return productRepository.getProductById(id);
    }

    public List<Long> exists(final List<Long> listId) {
        return productRepository.existsProduct(listId);
    }
}
