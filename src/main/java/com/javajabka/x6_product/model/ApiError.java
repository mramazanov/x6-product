package com.javajabka.x6_product.model;

import lombok.Getter;

@Getter
public class ApiError {

    private boolean success;
    private String message;

    public ApiError(final String message) {
        this.success = false;
        this.message = message;
    }
}