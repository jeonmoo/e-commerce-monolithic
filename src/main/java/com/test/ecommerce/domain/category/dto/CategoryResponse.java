package com.test.ecommerce.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class CategoryResponse implements Serializable {

    private Long id;
    private Long parentId;
    private Integer depth;
    private Integer sort;
    private String categoryName;
    private Boolean isDelete;
    List<CategoryResponse> subCategories = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
