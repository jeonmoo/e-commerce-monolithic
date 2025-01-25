package com.test.bootcamp.domain.product.dto;

import com.test.bootcamp.domain.order.enums.DiscountType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {
    private Long categoryId;
    private String productName;
    private BigDecimal finalPrice;
    private BigDecimal originPrice;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private Integer quantity;
    private Boolean isDelete;
}
