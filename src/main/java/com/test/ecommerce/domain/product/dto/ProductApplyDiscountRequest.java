package com.test.ecommerce.domain.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductApplyDiscountRequest {

    private BigDecimal discountPrice;
}
