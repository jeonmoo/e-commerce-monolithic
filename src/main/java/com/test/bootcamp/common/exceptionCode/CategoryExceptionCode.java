package com.test.bootcamp.common.exceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryExceptionCode implements ExceptionCode{

    NOT_FOUND_CATEGORY("NOT_FOUND_CATEGORY", "없는 카테고리 입니다."),

    ;
    private final String code;
    private final String message;
}
