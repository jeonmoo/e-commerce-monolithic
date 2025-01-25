package com.test.bootcamp.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.test.bootcamp.domain.order.enums.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductResponse {

    private Long id;
    private Long categoryId;
    private String productName;
    private BigDecimal finalPrice;
    private BigDecimal originPrice;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private Integer quantity;
    private Boolean isDelete;

    @QueryProjection
    public ProductResponse(Long id, Long categoryId, String productName, BigDecimal finalPrice, BigDecimal originPrice, DiscountType discountType, BigDecimal discountValue, Integer quantity, Boolean isDelete) {
        this.id = id;
        this.categoryId = categoryId;
        this.productName = productName;
        this.finalPrice = finalPrice;
        this.originPrice = originPrice;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.quantity = quantity;
        this.isDelete = isDelete;
    }
}
