package com.test.ecommerce.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ADMIN("ADMIN", "관리자"),
    USER("USER", "회원"),
    ;

    private final String name;
    private final String description;
}
