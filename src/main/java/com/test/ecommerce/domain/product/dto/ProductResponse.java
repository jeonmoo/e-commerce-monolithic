package com.test.ecommerce.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResponse {

    private Long id;
    private Long categoryId;
    private String productName;
    private Integer quantity;
    private BigDecimal finalPrice;
    private BigDecimal originPrice;
    private BigDecimal discountPrice;
    private Boolean isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection

    public ProductResponse(Long id, Long categoryId, String productName, Integer quantity, BigDecimal finalPrice, BigDecimal originPrice, BigDecimal discountPrice, Boolean isDelete, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.productName = productName;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.isDelete = isDelete;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
