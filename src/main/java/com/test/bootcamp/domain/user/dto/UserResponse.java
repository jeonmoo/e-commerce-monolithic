package com.test.bootcamp.domain.user.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;

    @Builder
    public UserResponse(Long id, String userName, String password, String phoneNumber, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
