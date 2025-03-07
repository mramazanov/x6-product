package com.javajabka.x6_product.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
}