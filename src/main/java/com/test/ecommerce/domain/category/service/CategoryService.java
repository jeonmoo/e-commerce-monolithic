package com.test.ecommerce.domain.category.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.CategoryExceptionCode;
import com.test.ecommerce.domain.category.dto.CategoryRequest;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Cacheable(value = "categoryList", key = "'allCategories'")
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "categoryList", key = "'allCategories'")
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    @Transactional
    @CacheEvict(value = "categoryList", key = "'allCategories'")
    public CategoryResponse modifyCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        updateCategory(category, request);
        return CategoryMapper.INSTANCE.toResponse(category);
    }

    @Transactional
    @CacheEvict(value = "categoryList", key = "'allCategories'")
    public void updateCategory(Category category, CategoryRequest request) {
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
