package com.test.ecommerce.domain.order.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.ecommerce.domain.order.dto.OrderHistoryRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.entity.Order;
import com.test.ecommerce.domain.order.enums.OrderStatus;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.test.ecommerce.domain.order.entity.QOrder.order;
import static com.test.ecommerce.domain.order.entity.QOrderItem.orderItem;
import static com.test.ecommerce.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Order> findByIdForUpdate(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(order)
                .where(order.id.eq(id))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }

    public Page<OrderResponse> findByOrdersByUserId(OrderHistoryRequest request, Pageable pageable) {
        List<OrderResponse> content = queryFactory.select(Projections.constructor(
                        OrderResponse.class
                        , order.id
                        , order.createdAt
                        , order.orderStatus
                        , product.productName
                        , orderItem.finalPrice
                        , orderItem.quantity
                ))
                .from(order)
                .leftJoin(orderItem)
                .leftJoin(product)
                .where(
                        userIdEq(request.getUserId())
                        , statusEq(request.getOrderStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(order.count())
                .from(order)
                .where(
                        userIdEq(request.getUserId())
                        , statusEq(request.getOrderStatus())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? order.user.id.eq(userId) : null;
    }

    private BooleanExpression statusEq(String statusCode) {
        if (!StringUtils.hasText(statusCode)) {
            return null;
        }

        OrderStatus status = OrderStatus.from(statusCode);
        return order.orderStatus.eq(status);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return List.of(new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, order.createdAt));
        }

        List<OrderSpecifier<?>> result = new ArrayList<>();

        for (Sort.Order sortOrder : pageable.getSort()) {
            com.querydsl.core.types.Order direction = sortOrder.getDirection().isAscending()
                    ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;

            OrderSpecifier<?> os = switch (sortOrder.getProperty()) {
                case "orderDate" -> new OrderSpecifier<>(direction, order.createdAt);
                case "status"    -> new OrderSpecifier<>(direction, order.orderStatus);
                case "totalPrice" -> new OrderSpecifier<>(direction, order.totalFinalPrice);
                case "productName" -> new OrderSpecifier<>(direction, product.productName);
                default -> new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, order.createdAt);
            };
            result.add(os);
        }

        return result;
    }
}
