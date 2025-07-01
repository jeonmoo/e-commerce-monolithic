package com.test.ecommerce.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class UserResponse implements Serializable {

    private Long id;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
}
