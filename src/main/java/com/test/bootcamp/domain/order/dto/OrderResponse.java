package com.test.bootcamp.domain.order.dto;

import com.test.bootcamp.domain.order.OrderStatus;
import com.test.bootcamp.domain.order.entity.Order;
import com.test.bootcamp.domain.user.entity.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private Long id;
    private User user;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private String address;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static class OrderItem {
        private Long id;
        private Order order;
        private Long productId;
        private BigDecimal price;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
