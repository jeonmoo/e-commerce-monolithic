package com.test.bootcamp.domain.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {
    private Long categoryId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private Boolean isDelete;
}
