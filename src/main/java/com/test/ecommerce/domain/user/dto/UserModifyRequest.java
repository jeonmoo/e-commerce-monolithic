package com.test.ecommerce.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserModifyRequest {

    @Size(min = 10, max = 20, message = "10 ~ 20 자 입니다.")
    private String phoneNumber;

    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;
}
