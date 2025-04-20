package com.test.ecommerce.domain.category.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.domain.category.dto.CategoryRequest;
import com.test.ecommerce.domain.category.dto.CategoryResponse;
import com.test.ecommerce.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> result = categoryService.getCategories();
        return ApiResponse.success(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.createCategory(request);
        return ApiResponse.success(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id,
                                                                        @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.modifyCategory(id, request);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> removeCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return ApiResponse.success(null);
    }

}
