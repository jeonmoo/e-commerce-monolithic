package com.test.ecommerce.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {

    private List<OrderRequestItem> orderItems;
    private Long userId;
    private String address;
    private String reason;
    @Getter
    public static class OrderRequestItem {
        private Long productId;
        private Integer quantity;
    }

}
