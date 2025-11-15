package com.test.ecommerce.domain.category.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.CategoryExceptionCode;
import com.test.ecommerce.domain.category.dto.CategoryCreateRequest;
import com.test.ecommerce.domain.category.dto.CategoryModifyRequest;
import com.test.ecommerce.domain.category.dto.CategoryResponse;
import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.mapper.CategoryMapper;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
import com.test.ecommerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Cacheable(value = "categoryList", key = "'allCategories'")
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        Map<Long, List<CategoryResponse>> categoryMap = categories.stream()
                .filter(category -> !category.getIsDelete())
                .map(CategoryMapper.INSTANCE::toResponse)
                .collect(Collectors.groupingBy(CategoryResponse::getParentId, TreeMap::new, Collectors.toList()));

        List<CategoryResponse> response = new ArrayList<>();
        categoryMap.forEach((k, v) -> {
            if (k.equals(0L)) {
                response.addAll(v);
                response.sort(Comparator.comparing(CategoryResponse::getSort));
            } else {
                v.sort(Comparator.comparing(CategoryResponse::getSort));
                response.forEach(parent -> {
                    if (parent.getId().equals(k)) {
                        parent.setSubCategories(v);
                    }
                });
            }
        });
        return response;
    }

    @Transactional
    @CacheEvict(value = "categoryList", key = "'allCategories'")
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    @Transactional
    @CacheEvict(value = "categoryList", key = "'allCategories'")
    public CategoryResponse modifyCategory(Long id, CategoryModifyRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        updateCategory(category, request);
        return CategoryMapper.INSTANCE.toResponse(category);
    }

    private void updateCategory(Category category, CategoryModifyRequest request) {
        category.setParentId(request.getParentId());
        category.setCategoryName(request.getCategoryName());
        category.setDepth(request.getDepth());
        category.setSort(request.getSort());
    }

    @Transactional
    @CacheEvict(value = "categoryList", key = "'allCategories'")
    public Boolean removeCategory(Long id) {
        Boolean isCategoryExist = productRepository.existsByCategoryId(id);
        if (isCategoryExist) {
            throw new GlobalException(CategoryExceptionCode.CATEGORY_HAS_PRODUCTS);
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        category.setIsDelete(true);
        return category.getIsDelete();
    }



}
