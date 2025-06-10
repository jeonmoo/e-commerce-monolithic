package com.test.ecommerce.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryRequest {

    private Long parentId;
    private Integer depth;
    private Integer sort;
    private String categoryName;
    private Boolean isDelete;
}
