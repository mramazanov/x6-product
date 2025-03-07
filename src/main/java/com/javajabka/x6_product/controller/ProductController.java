package com.javajabka.x6_product.controller;

import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.service.ProductService;
import com.javajabka.x6_product.model.ProductResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Tag(name = "Продукт")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Создать продукт")
    public ProductResponse createProduct(ProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    @PatchMapping
    @Operation(summary = "Обновить продукт")
    public ProductResponse updateProduct(final Long id, ProductRequest productRequest){
        return productService.updateProduct(id, productRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить продукт по id")
    public ProductResponse getUserById(@PathVariable final Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/exists")
    @Operation(summary = "Проверить существование продуктов по списку id")
    public List<Long> exists(@RequestParam final List<Long> ids) {
        return productService.exists(ids);
    }
}