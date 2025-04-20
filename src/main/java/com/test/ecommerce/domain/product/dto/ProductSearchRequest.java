package com.test.ecommerce.domain.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductSearchRequest {

    private String productName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Long categoryId;
    private Boolean isPriceAsc;
}
