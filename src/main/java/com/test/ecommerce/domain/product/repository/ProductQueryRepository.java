package com.test.ecommerce.domain.product.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.ecommerce.domain.product.dto.ProductSearchRequest;
import com.test.ecommerce.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.test.ecommerce.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Product> getProducts(ProductSearchRequest request, Pageable pageable) {
        List<Product> content = queryFactory.selectFrom(product)
                .where(
                        containName(request.getProductName())
                        , betweenFinalPrice(request.getMinPrice(), request.getMaxPrice())
                        , eqCategory(request.getCategoryId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(
                        containName(request.getProductName())
                        , betweenFinalPrice(request.getMinPrice(), request.getMaxPrice())
                        , eqCategory(request.getCategoryId())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return List.of(product.createdAt.desc());
        }

        List<OrderSpecifier<?>> result = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            // 타입을 명시적으로 지정하여 OrderSpecifier 생성
            OrderSpecifier<?> os = switch (order.getProperty()) {
                case "finalPrice" -> new OrderSpecifier<>(direction, product.finalPrice);
                case "productName" -> new OrderSpecifier<>(direction, product.productName);
                case "createdAt" -> new OrderSpecifier<>(direction, product.createdAt);
                default -> new OrderSpecifier<>(Order.DESC, product.createdAt);
            };
            result.add(os);
        }

        return result;
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
