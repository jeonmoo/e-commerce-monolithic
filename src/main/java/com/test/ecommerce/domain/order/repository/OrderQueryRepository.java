package com.test.ecommerce.domain.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.ecommerce.domain.order.entity.Order;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.test.ecommerce.domain.order.entity.QOrder.order;

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
}
