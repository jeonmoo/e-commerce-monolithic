package com.test.bootcamp.common.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCode implements ExceptionCode {

    NOT_FOUND_ORDER("NOT_FOUND_ORDER", "없는 주문입니다."),
    NOT_PENDING("NOT_PENDING", "주문대기 상태가 아닙니다."),
    ;

    private final String code;
    private final String message;

}
