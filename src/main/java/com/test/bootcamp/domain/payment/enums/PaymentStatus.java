package com.test.bootcamp.domain.payment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    PENDING("PENDING", "결제대기"),
    PAID("PAID", "결제완료"),
    PENDING_REFUND("PENDING_REFUND", "환불대기"),
    REFUND("REFUND", "환불"),
    CANCELED("CANCELED", "결제취소")
    ;

    private final String code;
    private final  String description;
}
