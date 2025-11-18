package com.test.ecommerce.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryModifyRequest {

    private Long parentId;
    private Integer depth;
    private Integer sort;
    private String categoryName;
}
