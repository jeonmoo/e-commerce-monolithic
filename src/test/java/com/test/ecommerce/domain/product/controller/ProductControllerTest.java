package com.test.ecommerce.domain.product.controller;

import com.test.ecommerce.config.TestContainerBase;
import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest extends TestContainerBase {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

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
        category = new Category();
        category.setDepth(0);
        category.setSort(0);
        category.setCategoryName("Digital");
        category.setIsDelete(false);
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("상품 등록 - 상품을 등록한다.")
    void createProductTest() throws Exception {
        Long categoryId = category.getId();
        String productName = "아이폰17";
        int quantity = 100;
        BigDecimal finalPrice = BigDecimal.valueOf(1000000);
        BigDecimal originPrice = BigDecimal.valueOf(1500000);
        BigDecimal discountPrice = BigDecimal.valueOf(500000);
        boolean isDelete = false;

        String requestBody = """
                {
                  "categoryId": %d,
                  "productName": "%s",
                  "quantity": %d,
                  "finalPrice": "%s",
                  "originPrice": "%s",
                  "discountPrice": "%s",
                  "isDelete": %b
                }
                """.formatted(categoryId, productName, quantity, finalPrice, originPrice, discountPrice, isDelete);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.categoryId").value(categoryId))
                .andExpect(jsonPath("$.result.productName").value(productName))
                .andExpect(jsonPath("$.result.quantity").value(quantity))
                .andExpect(jsonPath("$.result.finalPrice").value(finalPrice))
                .andExpect(jsonPath("$.result.originPrice").value(originPrice))
                .andExpect(jsonPath("$.result.discountPrice").value(discountPrice))
                .andExpect(jsonPath("$.result.isDelete").value(isDelete));
    }
}
