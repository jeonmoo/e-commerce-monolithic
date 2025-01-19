package com.test.bootcamp.domain.category.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.CategoryExceptionCode;
import com.test.bootcamp.domain.category.dto.CategoryRequest;
import com.test.bootcamp.domain.category.dto.CategoryResponse;
import com.test.bootcamp.domain.category.entity.Category;
import com.test.bootcamp.domain.category.mapper.CategoryMapper;
import com.test.bootcamp.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse modifyCategory(Long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow();
        updateCategory(category, request);
        return CategoryMapper.INSTANCE.toResponse(category);
    }

    @Transactional
    public void updateCategory(Category category, CategoryRequest request) {
        category.setParentId(request.getParentId());
        category.setCategoryName(request.getCategoryName());
        category.setDepth(request.getDepth());
        category.setSort(request.getSort());
    }

    @Transactional
    public void removeCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        category.setIsDelete(true);
    }



}
