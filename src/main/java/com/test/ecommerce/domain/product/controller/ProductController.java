package com.test.ecommerce.domain.product.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.domain.product.dto.ProductRequest;
import com.test.ecommerce.domain.product.dto.ProductResponse;
import com.test.ecommerce.domain.product.dto.ProductSearchRequest;
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

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long productId) {
        ProductResponse response = productService.getProductById(productId);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts() {
        List<ProductResponse> result = productService.getProducts();
        return ApiResponse.success(result);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(ProductSearchRequest request) {
        List<ProductResponse> result = productService.search(request);
        return ApiResponse.success(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ApiResponse.success(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long productId
            , @RequestBody ProductRequest request) {
        ProductResponse response = productService.modifyProduct(productId, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Object>> removeProduct(@PathVariable Long productId) {
        productService.removeProduct(productId);
        return ApiResponse.success(null);
    }

    @PostMapping("/{productId}/discount")
    public ResponseEntity<ApiResponse<Object>> applyDiscount(@PathVariable Long productId, @RequestBody ProductRequest request) {
        ProductResponse result = productService.applyDiscount(productId, request);
        return ApiResponse.success(result);
    }
}
