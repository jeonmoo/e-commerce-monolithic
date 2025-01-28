package com.test.bootcamp.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.test.bootcamp.domain.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResponse {

    private Long id;
    private Category category;
    private String productName;
    private Integer quantity;
    private BigDecimal finalPrice;
    private BigDecimal originPrice;
    private Long discountId;
    private Boolean isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public ProductResponse(Long id, Category category, String productName, Integer quantity, BigDecimal finalPrice, BigDecimal originPrice, Long discountId, Boolean isDelete, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.category = category;
        this.productName = productName;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
        this.originPrice = originPrice;
        this.discountId = discountId;
        this.isDelete = isDelete;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
