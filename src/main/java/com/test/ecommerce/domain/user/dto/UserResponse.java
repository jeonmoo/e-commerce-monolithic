package com.test.ecommerce.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
}
