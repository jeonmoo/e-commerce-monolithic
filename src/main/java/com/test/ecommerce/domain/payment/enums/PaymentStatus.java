package com.test.ecommerce.domain.payment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    PENDING("PENDING", "결제대기"),
    PAID("PAID", "결제완료"),
    PENDING_REFUND("PENDING_REFUND", "환불대기"),
    PARTIALLY_REFUNDED("PARTIALLY_REFUNDED", "부분 환불"),
    REFUND("REFUND", "환불"),
    CANCELED("CANCELED", "결제취소")
    ;

    private final String code;
    private final  String description;
}
