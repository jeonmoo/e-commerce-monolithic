package com.test.ecommerce.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryResponse {

    private Long id;
    private Long parentId;
    private Integer depth;
    private Integer sort;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
