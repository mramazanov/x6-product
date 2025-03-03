package com.javajabka.x6_product.controller;

import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.service.ProductService;
import com.javajabka.x6_product.model.ProductResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Tag(name = "Продукт")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Создать продукт")
    public ProductResponse createProduct(ProductRequest x6ProductRequest){
        return productService.createProduct(x6ProductRequest);
    }

    @PatchMapping
    @Operation(summary = "Обновить продукт")
    public ProductResponse updateProduct(final Long productId, ProductRequest x6ProductRequest){
        return productService.updateProduct(productId, x6ProductRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить продукт по id")
    public ProductResponse getUserById(@PathVariable final Long id) {
        return productService.getUserById(id);
    }

    @GetMapping("/exists")
    @Operation(summary = "Проверить существование продуктов по списку id")
    public List<Long> exists(@RequestParam final List<Long> listId) {
        return productService.exists(listId);
    }

}