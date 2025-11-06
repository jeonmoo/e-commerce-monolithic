package com.test.ecommerce.domain.order.service;

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
class OrderControllerTest extends TestContainerBase {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MockMvc mockMvc;

    private User user;
    private Product product;

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

        Category category = new Category();
        category.setDepth(0);
        category.setSort(0);
        category.setCategoryName("Digital");
        category.setIsDelete(false);
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
    @DisplayName("주문등록 - 주문을 등록한다")
    void createOrderTest() throws Exception {
        String requestBody = """
                {
                  "orderItems": [
                    {
                      "productId": %d,
                      "quantity": 10
                    }
                  ],
                  "userId": %d,
                  "address": "서울특별시 삼청동",
                  "reason": ""
                }
                """.formatted(product.getId(), user.getId());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.totalFinalPrice").value(product.getFinalPrice().multiply(BigDecimal.TEN).intValue()))
                .andExpect(jsonPath("$.result.orderStatus").value(OrderStatus.PENDING.getStatus()));
    }

    @Test
    @DisplayName("주문조회 - 주문을 조회한다")
    void getOrderTest() throws Exception {
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
        orderItem.setOrder(order);

        orderRepository.save(order);

        // when
        ResultActions response = mockMvc.perform(get("/orders/{orderId}", order.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.orderItems").isArray())
                .andExpect(jsonPath("$.result.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.result.address").value(address))
                .andExpect(jsonPath("$.result.totalFinalPrice").value(finalPrice))
                .andExpect(jsonPath("$.result.orderItems[0].productId").value(product.getId()));
    }

    @Test
    @DisplayName("주문 취소 - 주문을 취소한다.")
    void cancelOrderTest() throws Exception {
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
        orderItem.setOrder(order);

        orderRepository.save(order);

        String requestBody = """
                {
                  "reason": "%s"
                }
                """.formatted("단순변심");

        // when
        ResultActions response = mockMvc.perform(post("/orders/{id}/cancel", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

    @Test
    @DisplayName("주문 확정 - 주문을 확정한다.")
    void completeOrderTest() throws Exception {
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
        orderItem.setOrder(order);

        orderRepository.save(order);

        //when
        ResultActions response = mockMvc.perform(post("/orders/{id}/complete", order.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.orderStatus").value(OrderStatus.COMPLETE.getStatus()));
    }

    @Test
    @DisplayName("주문 전체 환불 요청 - 주문 전체 환불요청을 한다.")
    void requestRefundOrderTest() throws Exception {
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
        ResultActions response = mockMvc.perform(post("/orders/{id}/refund-request", order.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.orderStatus").value(OrderStatus.REQUIRED_REFUND.getStatus()));
    }

    @Test
    @DisplayName("주문 전체 환불 - 주문 전체를 환불한다.")
    void refundOrderTest() throws Exception {
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
        order.setOrderStatus(OrderStatus.REQUIRED_REFUND);  // 환불요청상태에서 환불가능
        orderItem.setOrder(order);

        orderRepository.save(order);

        // when
        ResultActions response = mockMvc.perform(post("/orders/{id}/refund", order.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.orderStatus").value(OrderStatus.REFUNDED.getStatus()))
                .andExpect(jsonPath("$.result.orderItems[*].orderStatus").value(OrderStatus.REFUNDED.getStatus()));
    }

    @Test
    @DisplayName("주문 부분 환불 요청 - 주문 부분 환불요청을 한다.")
    void requestRefundOrderItemTest() throws Exception {
        // given
        Integer quantity = 10;
        BigDecimal originPrice = BigDecimal.valueOf(100000);
        BigDecimal finalPrice = BigDecimal.valueOf(80000);
        BigDecimal discountPrice = BigDecimal.valueOf(20000);
        String address = "경기도 성남시 분당구 정자일로 95";

        OrderItem orderItem1 = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .originPrice(originPrice)
                .finalPrice(finalPrice)
                .discountPrice(discountPrice)
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .originPrice(originPrice)
                .finalPrice(finalPrice)
                .discountPrice(discountPrice)
                .build();

        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .totalFinalPrice(finalPrice)
                .totalOriginPrice(originPrice)
                .totalDiscountPrice(discountPrice)
                .address(address)
                .build();
        order.setOrderStatus(OrderStatus.COMPLETE);
        orderItems.forEach(item -> {
            item.setOrder(order);
            item.setOrderStatus(OrderStatus.COMPLETE);
        });

        orderRepository.save(order);

        // when
        ResultActions response = mockMvc.perform(post("/orders/{id}/{orderItemId}/refund-request", order.getId(), orderItem1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.orderStatus").value(OrderStatus.PARTIALLY_REQUIRED_REFUND.getStatus()))
                .andExpect(jsonPath("$.result.orderItems[?(@.id == " + orderItem1.getId() + ")]").exists())
                .andExpect(jsonPath("$.result.orderItems[?(@.id == " + orderItem1.getId() + ")].orderStatus").value(OrderStatus.REQUIRED_REFUND.getStatus()));
    }

}
