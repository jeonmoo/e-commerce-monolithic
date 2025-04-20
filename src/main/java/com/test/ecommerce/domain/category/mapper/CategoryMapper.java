package com.test.ecommerce.domain.category.mapper;

import com.test.ecommerce.domain.category.dto.CategoryRequest;
import com.test.ecommerce.domain.category.dto.CategoryResponse;
import com.test.ecommerce.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);
}
