package com.test.ecommerce.domain.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("PENDING", "주문대기"),
    COMPLETE("COMPLETE", "주문완료"),
    CANCELED("CANCELED", "주문취소"),
    PARTIALLY_REQUIRED_REFUND("PARTIALLY_REQUIRED_REFUND", "부분환불요청"),
    REQUIRED_REFUND("REQUIRED_REFUND", "환불요청"),
    PARTIALLY_REFUNDED("PARTIALLY_REFUNDED", "부분환불"),
    REFUNDED("REFUNDED", "환불"),
    ;

    private final String status;
    private final String description;
}
