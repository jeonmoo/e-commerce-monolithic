package com.test.ecommerce.domain.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {

    private Long productId;
    private Long categoryId;
    private String productName;
    private Integer quantity;
    private BigDecimal finalPrice;
    private BigDecimal originPrice;
    private Long discountId;
    private Boolean isDelete;
}
