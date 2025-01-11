package com.test.bootcamp.domain.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {
    private Long id;
    private String productName;
    private BigDecimal price;
}
