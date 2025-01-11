package com.test.bootcamp.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("PENDING", "주문대기"),
    CONFIRMED("CONFIRMED", "주문확정")
    ;

    private final String status;
    private final String description;
}
