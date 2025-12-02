package com.test.ecommerce.domain.product.dto;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
public class ProductModifyRequest {

    private Long categoryId;
    private String productName;
    private Integer quantity;
    private BigDecimal finalPrice;
    private BigDecimal originPrice;
    private BigDecimal discountPrice;
}
