package com.test.ecommerce.common.exceptionCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserExceptionCode implements ExceptionCode {
    NOT_FOUND_USER("NOT_FOUND_USER", "없는 회원입니다."),
    DUPLICATE_USER("DUPLICATE_USER", "중복된 회원입니다."),
    INVALID_USER_PASSWORD("INVALID_USER_PASSWORD", "잘못된 비밀번호입니다.")
    ;

    private final String code;
    private final String message;
}
