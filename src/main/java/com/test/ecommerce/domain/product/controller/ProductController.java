package com.test.ecommerce.domain.product.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.domain.product.dto.*;
import com.test.ecommerce.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(@ModelAttribute ProductSearchRequest request) {
        List<ProductResponse> result = productService.getProducts(request);
        return ApiResponse.success(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> modifyProduct(@PathVariable Long id
            , @RequestBody ProductModifyRequest request) {
        ProductResponse response = productService.modifyProduct(id, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> removeProduct(@PathVariable Long id) {
        productService.removeProduct(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/discount")
    public ResponseEntity<ApiResponse<ProductResponse>> applyDiscount(@PathVariable Long id, @RequestBody ProductApplyDiscountRequest request) {
        ProductResponse result = productService.applyDiscount(id, request);
        return ApiResponse.success(result);
    }

    @GetMapping("/rank")
    public ResponseEntity<ApiResponse<List<ProductRankResponse>>> getProductRank(@RequestParam Long startIndex, @RequestParam Long endIndex) {
        List<ProductRankResponse> result = productService.getProductRank(startIndex, endIndex);
        return ApiResponse.success(result);
    }
}
