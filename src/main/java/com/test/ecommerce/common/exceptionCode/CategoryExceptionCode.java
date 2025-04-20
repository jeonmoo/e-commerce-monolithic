package com.test.ecommerce.common.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryExceptionCode implements ExceptionCode {

    NOT_FOUND_CATEGORY("NOT_FOUND_CATEGORY", "없는 카테고리 입니다."),
    CATEGORY_HAS_PRODUCTS("CATEGORY_HAS_PRODUCTS", "해당 카테고리의 상품이 존재합니다."),

    ;
    private final String code;
    private final String message;
}
