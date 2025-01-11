package com.test.bootcamp.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rule {

    ADMIN("ADMIN", "관리자"),
    USER("USER", "회원"),
    ;

    private final String name;
    private final String description;
}
