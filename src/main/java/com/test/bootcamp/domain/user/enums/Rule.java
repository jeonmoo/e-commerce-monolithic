package com.test.bootcamp.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rule {

    ADMIN("ADMIN", "관리자"),
    USER("USER", "회원"),
    ;

    private final String name;
    private final String description;
}
