package com.test.ecommerce.domain.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.ecommerce.domain.product.dto.ProductSearchRequest;
import com.test.ecommerce.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.test.ecommerce.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Product> getProducts(ProductSearchRequest request) {
//        OrderSpecifier orderSpecifier = request.getIsPriceAsc() ? product..asc() : product.price.desc();
        return queryFactory.selectFrom(product)
                .where(
                        containName(request.getProductName())
                        , betweenFinalPrice(request.getMinPrice(), request.getMaxPrice())
                        , eqCategory(request.getCategoryId())
                )
//                .orderBy(
//                        orderSpecifier
//                )
                .fetch();
    }

    private BooleanExpression containName(String name) {
        return StringUtils.hasText(name) ? product.productName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression betweenFinalPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        BooleanExpression condition = null;

        if (Objects.nonNull(minPrice)) {
            condition = product.finalPrice.goe(minPrice);
        }
        if (Objects.nonNull(maxPrice)) {
            condition = Objects.isNull(condition) ? product.finalPrice.loe(maxPrice) : condition.and(product.finalPrice.loe(maxPrice));
        }
        return condition;
    }

    private BooleanExpression eqCategory(Long categoryId) {
        return Objects.nonNull(categoryId) ? product.category.id.eq(categoryId) : null;
    }
}
