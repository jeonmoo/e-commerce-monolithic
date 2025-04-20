package com.test.ecommerce.domain.category.dto;

import lombok.Getter;

@Getter
public class CategoryRequest {

    private Long parentId;
    private Integer depth;
    private Integer sort;
    private String categoryName;
}
