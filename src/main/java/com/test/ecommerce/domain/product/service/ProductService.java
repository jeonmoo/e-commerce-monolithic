package com.test.ecommerce.domain.product.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.CategoryExceptionCode;
import com.test.ecommerce.common.exceptionCode.ProductExceptionCode;
import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
import com.test.ecommerce.domain.discount.repository.DiscountRepository;
import com.test.ecommerce.domain.product.dto.ProductRequest;
import com.test.ecommerce.domain.product.dto.ProductResponse;
import com.test.ecommerce.domain.product.dto.ProductSearchRequest;
import com.test.ecommerce.domain.product.entity.Product;
import com.test.ecommerce.domain.product.mapper.ProductMapper;
import com.test.ecommerce.domain.product.repository.ProductQueryRepository;
import com.test.ecommerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductSupportService productSupportService;
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    private final CategoryRepository categoryRepository;

    private final DiscountRepository discountRepository;

    private Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ProductExceptionCode.NOT_FOUND_PRODUCT));
    }

    @Cacheable(value = "productDetail", key = "#id")
    public ProductResponse getProductById(Long id) {
        Product product = findById(id);
        return ProductMapper.INSTANCE.toResponse(product);
    }

    public List<ProductResponse> getProducts(ProductSearchRequest request) {
        List<Product> products = productQueryRepository.getProducts(request);
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
    public ProductResponse modifyProduct(Long id, ProductRequest request) {
        Product product = findById(id);
        updateProduct(product, request);
        return ProductMapper.INSTANCE.toResponse(product);
    }

    @Transactional
    public void removeProduct(Long id) {
        Product product = findById(id);
        product.setIsDelete(true);
    }

    @Transactional
    public void updateProduct(Product product, ProductRequest request) {
        Long categoryId = request.getCategoryId();
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        }

        product.setCategory(category);
        product.setDiscountPrice(request.getDiscountPrice());
        product.setProductName(request.getProductName());
        product.setQuantity(request.getQuantity());
        product.setFinalPrice(request.getFinalPrice());
        product.setOriginPrice(request.getOriginPrice());
        product.setFinalPrice(request.getFinalPrice());
    }

    @Transactional
    public ProductResponse applyDiscount(Long id, ProductRequest request) {
        Product product = findById(id);
        BigDecimal discountPrice = request.getDiscountPrice();
        productSupportService.initDiscountToProduct(product, discountPrice);

        return ProductMapper.INSTANCE.toResponse(product);
    }
}
