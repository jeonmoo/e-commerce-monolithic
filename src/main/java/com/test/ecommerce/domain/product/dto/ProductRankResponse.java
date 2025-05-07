package com.test.ecommerce.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRankResponse {
    private Long id;
    private String name;
    private Long rank;
    private Long score;
}
