package com.test.ecommerce.domain.user.controller;

import com.test.ecommerce.config.TestContainerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends TestContainerBase {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void clear() {
        // 메서드 실행 전 캐시 초기화
        redisTemplate.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushAll();
    }

    @Test
    @Disabled
    @DisplayName("사용자 회원가입 - 사용자 회원가입을 한다.")
    void createUserTest() throws Exception {
        String userName = "테스트유저";
        String password = "test1234";
        String phoneNumber = "01000000000";
        String email = "test@test.com";

        String requestBody = """
                {
                  "userName": "%s",
                  "password": "%s",
                  "phoneNumber": "%s",
                  "email": "%s"
                }
                """.formatted(userName, password, phoneNumber, email);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.userName").value(userName))
                .andExpect(jsonPath("$.result.password").value(password))
                .andExpect(jsonPath("$.result.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.result.email").value(email));
    }

}
