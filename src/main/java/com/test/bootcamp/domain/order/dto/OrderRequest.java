package com.test.bootcamp.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {

    private List<OrderItem> orderItems;
    private Long userId;
    private String address;
    private String reason;
    @Getter
    public static class OrderItem {
        private Long productId;
        private Integer quantity;
    }

}
