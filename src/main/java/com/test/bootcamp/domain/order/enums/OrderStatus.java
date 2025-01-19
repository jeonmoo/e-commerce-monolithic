package com.test.bootcamp.domain.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("PENDING", "주문대기"),
    COMPLETE("COMPLETE", "주문완료"),
    REFUND("REFUND", "주문완료"),
    CANCELED("CANCELED", "주문취소")
    ;

    private final String status;
    private final String description;
}
