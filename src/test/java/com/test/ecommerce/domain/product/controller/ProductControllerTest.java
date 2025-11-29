package com.test.ecommerce.domain.product.controller;

import com.test.ecommerce.config.TestContainerBase;
import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
import com.test.ecommerce.domain.product.entity.Product;
import com.test.ecommerce.domain.product.repository.ProductRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private ProductRepository productRepository;

    private Category category;

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
        category = Category.builder()
                .depth(0)
                .sort(0)
                .categoryName("Digital")
                .build();
        categoryRepository.save(category);

        product = Product.builder()
                .category(category)
                .productName("테스트 상품")
                .quantity(100)
                .originPrice(BigDecimal.valueOf(10000))
                .discountPrice(BigDecimal.valueOf(2000))
                .finalPrice(BigDecimal.valueOf(8000))
                .build();
        productRepository.save(product);
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
                  "discountPrice": "%s"
                }
                """.formatted(categoryId, productName, quantity, finalPrice, originPrice, discountPrice);

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

    @Test
    @DisplayName("상품 조회 - 상품을 조회한다")
    void getProductTest() throws Exception {
        // when
        ResultActions response = mockMvc.perform(get("/product/{productId}", product.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.id").value(product.getId()))
                .andExpect(jsonPath("$.result.categoryId").value(category.getId()))
                .andExpect(jsonPath("$.result.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.result.quantity").value(product.getQuantity()))
                .andExpect(jsonPath("$.result.finalPrice").value(product.getFinalPrice()))
                .andExpect(jsonPath("$.result.isDelete").value(false));
    }

    @Test
    @DisplayName("상품 수정 - 상품을 수정한다.")
    void modifyProductTest() throws Exception {
        // given
        String productName = "변경된 상품명";
        Integer quantity = 20;
        BigDecimal finalPrice = BigDecimal.valueOf(20000);
        BigDecimal originPrice = BigDecimal.valueOf(16000);
        BigDecimal discountPrice = BigDecimal.valueOf(4000);

        String requestBody = """
                {
                  "categoryId": %d,
                  "productName": "%s",
                  "quantity": %d,
                  "finalPrice": %f,
                  "originPrice": %f,
                  "discountPrice": %f
                }
                """.formatted(category.getId(), productName, quantity, finalPrice, originPrice, discountPrice);

        ResultActions response = mockMvc.perform(put("/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.productName").value(productName))
                .andExpect(jsonPath("$.result.quantity").value(quantity))
                .andExpect(jsonPath("$.result.finalPrice").value(finalPrice.doubleValue()))
                .andExpect(jsonPath("$.result.originPrice").value(originPrice.doubleValue()))
                .andExpect(jsonPath("$.result.discountPrice").value(discountPrice.doubleValue()));
    }

    @Test
    @DisplayName("상품 삭제 - 상품을 삭제한다.")
    void removeProductTest() throws Exception {
        // given & when
        ResultActions response = mockMvc.perform(delete("/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));

        Product deletedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(deletedProduct.getIsDelete())
                .as("삭제처리는 Product.isDelete == true")
                .isTrue();
    }

    @Test
    @DisplayName("상품 리스트 조회 - 상품 리스트를 조회한다.")
    void getProductsTest() throws Exception {
        // given
        String requestBody = """
                {
                  "productName": "%s",
                  "minPrice": %d,
                  "maxPrice": %d,
                  "categoryId": %d,
                  "isPriceAsc": %b
                }
                """.formatted(product.getProductName(), 0, 100000, category.getId(), true);

        // when
        ResultActions response = mockMvc.perform(get("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].id").value(product.getId()));
    }

    @Test
    @DisplayName("상품 할인 적용(수정) - 상품 할인을 적용(수정)한다.")
    void applyDiscountTest() throws Exception {
        // given
        BigDecimal discountPrice = BigDecimal.valueOf(5000);
        String requestBody = """
                {
                  "discountPrice": %f
                }
                """.formatted(discountPrice);

        // when
        ResultActions response = mockMvc.perform(post("/product/{id}/discount", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.discountPrice").value(discountPrice.doubleValue()))
                .andExpect(jsonPath("$.result.finalPrice").value(product.getOriginPrice().subtract(discountPrice).doubleValue()));
    }
}
