package com.test.ecommerce.common.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductExceptionCode implements ExceptionCode {
    NOT_FOUND_PRODUCT("NOT_FOUND_PRODUCT", "없는 상품입니다."),
    OUT_OF_STOCK("OUT_OF_STOCK", "재고가 없습니다."),
    ;

    private final String code;
    private final String message;
}
