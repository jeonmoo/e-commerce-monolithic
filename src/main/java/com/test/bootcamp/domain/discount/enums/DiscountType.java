package com.test.bootcamp.domain.discount.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType {

    COMMON("COMMON", "일반상품할인"),
    AMOUNT("AMOUNT", "정액할인"),
    RATE("RATE", "정률할인"),
    ;

    private final String code;
    private final String description;
}
