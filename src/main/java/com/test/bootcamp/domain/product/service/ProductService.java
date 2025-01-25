package com.test.bootcamp.domain.product.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.CategoryExceptionCode;
import com.test.bootcamp.common.exceptionCode.ProductExceptionCode;
import com.test.bootcamp.domain.category.entity.Category;
import com.test.bootcamp.domain.category.repository.CategoryRepository;
import com.test.bootcamp.domain.product.dto.ProductRequest;
import com.test.bootcamp.domain.product.dto.ProductResponse;
import com.test.bootcamp.domain.product.dto.ProductSearchRequest;
import com.test.bootcamp.domain.product.entity.Product;
import com.test.bootcamp.domain.product.mapper.ProductMapper;
import com.test.bootcamp.domain.product.repository.ProductQueryRepository;
import com.test.bootcamp.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ProductExceptionCode.NOT_FOUND_PRODUCT));
        return ProductMapper.INSTANCE.toResponse(product);
    }

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper.INSTANCE::toResponse)
                .toList();
    }

    public List<ProductResponse> search(ProductSearchRequest request) {
        List<Product> products = productQueryRepository.search(request);
        return products.stream()
                .map(ProductMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        Product product = ProductMapper.INSTANCE.toProduct(request);
        product.setCategory(category);
        productRepository.save(product);
        return ProductMapper.INSTANCE.toResponse(product);
    }

    @Transactional
    public ProductResponse modifyProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->new GlobalException(ProductExceptionCode.NOT_FOUND_PRODUCT));
        updateProduct(product, request);
        return ProductMapper.INSTANCE.toResponse(product);
    }

    @Transactional
    public void removeProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ProductExceptionCode.NOT_FOUND_PRODUCT));
        product.setIsDelete(true);
    }

    @Transactional
    public void updateProduct(Product product, ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        product.setProductName(request.getProductName());
        product.setQuantity(request.getQuantity());
        product.setFinalPrice(request.getFinalPrice());
        product.setOriginPrice(request.getOriginPrice());
        product.setDiscountType(request.getDiscountType());
        product.setDiscountValue(request.getDiscountValue());
        product.setFinalPrice(request.getFinalPrice());
        product.setCategory(category);
    }

}
