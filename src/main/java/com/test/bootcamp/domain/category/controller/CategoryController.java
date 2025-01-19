package com.test.bootcamp.domain.category.controller;

import com.test.bootcamp.common.ApiResponse;
import com.test.bootcamp.domain.category.dto.CategoryRequest;
import com.test.bootcamp.domain.category.dto.CategoryResponse;
import com.test.bootcamp.domain.category.service.CategoryService;
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

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long categoryId,
                                                                        @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.modifyCategory(categoryId, request);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Object>> removeCategory(@PathVariable Long categoryId) {
        categoryService.removeCategory(categoryId);
        return ApiResponse.success(null);
    }

}
