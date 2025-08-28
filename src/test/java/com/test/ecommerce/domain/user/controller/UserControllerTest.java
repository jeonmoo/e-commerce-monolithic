package com.test.ecommerce.domain.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

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

    @BeforeEach
    void setData() {
        
    }

}
