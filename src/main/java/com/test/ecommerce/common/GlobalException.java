package com.test.ecommerce.common;

import com.test.ecommerce.common.exceptionCode.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {

    private String code;
    private String message;

    public GlobalException(ExceptionCode exception) {
        this.code = exception.getCode();
        this.message = exception.getMessage();
    }
}
