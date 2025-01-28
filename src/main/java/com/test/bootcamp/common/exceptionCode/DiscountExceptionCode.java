package com.test.bootcamp.common.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountExceptionCode implements ExceptionCode {

    NOT_FOUND_DISCOUNT("NOT_FOUND_DISCOUNT", "없는 할인입니다."),
    NOT_COMMON_TYPE("NOT_COMMON_TYPE", "일반 할인만 상품에 적용 가능합니다."),
    ;

    private final String code;
    private final String message;

}
