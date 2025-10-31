package com.test.ecommerce.domain.order.dto;

import com.test.ecommerce.domain.order.enums.OrderStatus;
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
    private List<OrderResponseItem> orderItems;
    private OrderStatus orderStatus;
    private String address;
    private BigDecimal totalFinalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class OrderResponseItem {
        private Long id;
        private Long productId;
        private OrderStatus orderStatus;
        private BigDecimal finalPrice;
        private Integer quantity;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }

}
