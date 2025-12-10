package com.test.ecommerce.domain.user.controller;

import com.test.ecommerce.config.TestContainerBase;
import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
import com.test.ecommerce.domain.order.entity.Order;
import com.test.ecommerce.domain.order.entity.OrderItem;
import com.test.ecommerce.domain.order.enums.OrderStatus;
import com.test.ecommerce.domain.order.repository.OrderRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    User user;

    Product product;

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
        user = User.builder()
                .userRole(UserRole.USER)
                .userName("테스트 유저")
                .password("test")
                .phoneNumber("010-0000-0000")
                .email("test@test.com")
                .build();
        userRepository.save(user);

        Category category = Category.builder()
                .depth(0)
                .sort(0)
                .categoryName("Digital")
                .build();
        categoryRepository.save(category);

        product = Product.builder()
                .productName("Macbook M1 Air")
                .category(category)
                .quantity(100)
                .originPrice(BigDecimal.valueOf(10000))
                .discountPrice(BigDecimal.ZERO)
                .finalPrice(BigDecimal.valueOf(10000))
                .build();
        productRepository.save(product);
    }

    @Test
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

    @Test
    @DisplayName("사용자 조회 - 사용자를 조회한다.")
    void getUser() throws Exception {
        // when
        ResultActions response = mockMvc.perform(get("/user/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.result.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.result.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("사용자 정보 수정 - 사용자 정보를 수정한다.")
    void modifyUserTest() throws Exception {
        // given
        String phoneNumber = "01011112222";
        String email = "test2@test.com";

        String requestBody = """
                {
                  "userName": "테스트유저",
                  "password": "test",
                  "phoneNumber": "%s",
                  "email": "%s"
                }
                """.formatted(phoneNumber, email);

        // when
        ResultActions response = mockMvc.perform(put("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.result.email").value(email));
    }

    @Test
    @DisplayName("사용자 주문 조회 - 사용자의 주문을 확인한다.")
    void getOrdersTest() throws Exception {
        // given
        Integer quantity = 10;
        BigDecimal originPrice = BigDecimal.valueOf(100000);
        BigDecimal finalPrice = BigDecimal.valueOf(80000);
        BigDecimal discountPrice = BigDecimal.valueOf(20000);
        String address = "경기도 성남시 분당구 정자일로 95";

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .originPrice(originPrice)
                .finalPrice(finalPrice)
                .discountPrice(discountPrice)
                .build();

        Order order = Order.builder()
                .user(user)
                .orderItems(List.of(orderItem))
                .totalFinalPrice(finalPrice)
                .totalOriginPrice(originPrice)
                .totalDiscountPrice(discountPrice)
                .address(address)
                .build();
        order.setOrderStatus(OrderStatus.COMPLETE);
        orderItem.setOrder(order);

        orderRepository.save(order);

        // when
        ResultActions response = mockMvc.perform(get("/user/{id}/orders", user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].id").value(order.getId()));
    }

    @Test
    @DisplayName("사용자 환불 내역 조회 - 사용자의 환불내역을 조회한다.")
    void getRefundsTest() throws Exception {
        // given
        Integer quantity = 10;
        BigDecimal originPrice = BigDecimal.valueOf(100000);
        BigDecimal finalPrice = BigDecimal.valueOf(80000);
        BigDecimal discountPrice = BigDecimal.valueOf(20000);
        String address = "경기도 성남시 분당구 정자일로 95";

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .originPrice(originPrice)
                .finalPrice(finalPrice)
                .discountPrice(discountPrice)
                .build();

        Order order = Order.builder()
                .user(user)
                .orderItems(List.of(orderItem))
                .totalFinalPrice(finalPrice)
                .totalOriginPrice(originPrice)
                .totalDiscountPrice(discountPrice)
                .address(address)
                .build();
        order.setOrderStatus(OrderStatus.REFUNDED);
        orderItem.setOrder(order);

        orderRepository.save(order);

        // when
        ResultActions response = mockMvc.perform(get("/user/{id}/orders/refunds", user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].id").value(order.getId()));
    }

}
