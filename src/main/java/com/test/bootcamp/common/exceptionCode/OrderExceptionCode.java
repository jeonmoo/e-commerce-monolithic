package com.test.bootcamp.common.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCode implements ExceptionCode {

    NOT_FOUND_ORDER("NOT_FOUND_ORDER", "없는 주문 입니다."),
    NOT_FOUND_ORDER_Item("NOT_FOUND_ORDER_Item", "없는 주문상세 입니다."),
    NOT_PENDING("NOT_PENDING", "주문대기 상태가 아닙니다."),
    INVALID_REFUND_REQUEST("INVALID_REFUND_REQUEST", "잘못된 환불요청 입니다."),
    NOT_REFUNDABLE_ORDER("NOT_REFUNDABLE_ORDER", "환불 할 수 없는 주문 입니다."),
    NOT_REFUNDABLE_ORDER_ITEM("NOT_REFUNDABLE_ORDER", "부분 환불 할 수 없는 주문 상세 입니다."),
    ;

    private final String code;
    private final String message;

}
