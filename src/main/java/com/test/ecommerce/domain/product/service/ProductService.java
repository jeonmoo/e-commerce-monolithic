package com.test.ecommerce.domain.product.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.CategoryExceptionCode;
import com.test.ecommerce.common.exceptionCode.ProductExceptionCode;
import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
import com.test.ecommerce.domain.product.dto.*;
import com.test.ecommerce.domain.product.entity.Product;
import com.test.ecommerce.domain.product.mapper.ProductMapper;
import com.test.ecommerce.domain.product.repository.ProductQueryRepository;
import com.test.ecommerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductSupportService productSupportService;
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    private final CategoryRepository categoryRepository;
    private final StringRedisTemplate stringRedisTemplate;

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
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new GlobalException(CategoryExceptionCode.NOT_FOUND_CATEGORY));
        Product product = ProductMapper.INSTANCE.toProduct(request);
        product.setCategory(category);
        productRepository.save(product);
        return ProductMapper.INSTANCE.toResponse(product);
    }

    @Transactional
    @CachePut(value = "productDetail", key = "#id")
    public ProductResponse modifyProduct(Long id, ProductModifyRequest request) {
        Product product = findById(id);
        updateProduct(product, request);
        return ProductMapper.INSTANCE.toResponse(product);
    }

    @Transactional
    @CacheEvict(value = "productDetail", key = "#id")
    public void removeProduct(Long id) {
        Product product = findById(id);
        product.setIsDelete(true);
    }

    @Transactional
    public void updateProduct(Product product, ProductModifyRequest request) {
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
    public ProductResponse applyDiscount(Long id, ProductApplyDiscountRequest request) {
        Product product = findById(id);
        BigDecimal discountPrice = request.getDiscountPrice();
        productSupportService.initDiscountToProduct(product, discountPrice);

        return ProductMapper.INSTANCE.toResponse(product);
    }

    public List<ProductRankResponse> getProductRank(long startIndex, long EndIndex) {
        String key = "product:score";
        Set<ZSetOperations.TypedTuple<String>> topProducts = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(key, startIndex, EndIndex);

        AtomicLong rank = new AtomicLong(1);
        return topProducts.stream()
                .map(tuple -> {
                    long id = Long.parseLong(tuple.getValue());
                    String name = findById(id).getProductName();
                    long score = tuple.getScore().longValue();
                    return ProductMapper.INSTANCE.toRankResponse(id, name, rank.getAndIncrement(), score);
                })
                .toList();
    }
}
