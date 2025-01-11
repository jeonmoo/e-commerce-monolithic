package com.test.bootcamp.domain.user.dto;

import lombok.Getter;

@Getter
public class UserRequest {
    private Long id;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
}
