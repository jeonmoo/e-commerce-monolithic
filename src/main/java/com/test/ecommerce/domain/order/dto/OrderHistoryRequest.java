package com.test.ecommerce.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryRequest {
    @Schema(hidden = true)
    private Long userId;
    private Long orderId;
    private LocalDateTime createdAt;
    private String orderStatus;
    private String productName;

    public void assignUserId(Long userId) {
        this.userId = userId;
    }
}
