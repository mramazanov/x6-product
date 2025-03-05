package com.javajabka.x6_product.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse implements Serializable {
    private final Long id;
    private final String name;
    private final BigDecimal price;
}