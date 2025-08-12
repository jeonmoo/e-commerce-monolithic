package com.test.ecommerce.domain.order.service;

import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
import com.test.ecommerce.domain.product.entity.Product;
import com.test.ecommerce.domain.product.repository.ProductRepository;
import com.test.ecommerce.domain.user.entity.User;
import com.test.ecommerce.domain.user.enums.UserRole;
import com.test.ecommerce.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

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
        User user = User.builder()
                .userRole(UserRole.USER)
                .userName("테스트 유저")
                .password("test")
                .phoneNumber("010-0000-0000")
                .email("test@test.com")
                .build();
        userRepository.save(user);

        Category category = new Category();
        category.setDepth(0);
        category.setSort(0);
        category.setCategoryName("Digital");
        category.setIsDelete(false);
        categoryRepository.save(category);

        Product product = Product.builder()
                .productName("Macbook M1 Air")
                .category(category)
                .quantity(100)
                .originPrice(BigDecimal.valueOf(10000))
                .discountPrice(BigDecimal.ZERO)
                .finalPrice(BigDecimal.valueOf(10000))
                .isDelete(false)
                .build();
        productRepository.save(product);
    }

    @Test
    @DisplayName("주문등록 - 주문을 등록한다")
    void registerOrderTest() {

    }
}
