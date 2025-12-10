package com.test.ecommerce.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserCreateRequest {

    @Size(max = 50, message = "최대 50자 이하여야 합니다.")
    @NotBlank
    private String userName;

    @Size(min = 4,max = 20, message = "비밀번호는 4~20자 입니다.")
    private String password;

    @Size(min = 10, max = 20, message = "10 ~ 20 자 입니다.")
    private String phoneNumber;

    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;
}
