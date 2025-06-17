package com.test.ecommerce.domain.order.repository;

import com.test.ecommerce.domain.order.entity.Order;
import com.test.ecommerce.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems"})
    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
