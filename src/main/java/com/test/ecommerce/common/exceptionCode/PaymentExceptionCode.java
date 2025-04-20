package com.test.ecommerce.common.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionCode implements ExceptionCode {

    NOT_FOUND_PAYMENT("NOT_FOUND_PAYMENT", "결제내역이 없습니다."),
    NOT_PAID("NOT_PAID", "결제상태가 아닙니다."),
    NOT_PENDING_REFUND("NOT_PENDING_REFUND", "결제대기가 아닙니다."),
    ;

    private final String code;
    private final String message;
}
