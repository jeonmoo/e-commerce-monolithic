package com.test.ecommerce.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryResponse implements Serializable {

    private Long id;
    private Long parentId;
    private Integer depth;
    private Integer sort;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
