package com.test.bootcamp.domain.order.dto;

import com.test.bootcamp.domain.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {

    private Long id;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private String address;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class OrderItem {
        private Long id;
        private Long productId;
        private BigDecimal price;
        private Integer quantity;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }

}
