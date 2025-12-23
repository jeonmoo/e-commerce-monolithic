package com.test.ecommerce.domain.category.controller;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest extends TestContainerBase {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    MockMvc mockMvc;

    Category category;

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
                .parentId(0L)
                .depth(0)
                .sort(0)
                .categoryName("Digital")
                .build();
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("카테고리 생성 - 카테고리를 생성한다.")
    void createCategoryTest() throws Exception {
        // given
        int parentId = 0;
        int depth = 1;
        int sort = 1;
        String categoryName = "IT";

        String requestBody = """
                {
                  "parentId": %d,
                  "depth": %d,
                  "sort": %d,
                  "categoryName": "%s"
                }
                """.formatted(parentId, depth, sort, categoryName);

        // when
        ResultActions response = mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.parentId").value(parentId))
                .andExpect(jsonPath("$.result.depth").value(depth))
                .andExpect(jsonPath("$.result.sort").value(sort))
                .andExpect(jsonPath("$.result.categoryName").value(categoryName))
                .andExpect(jsonPath("$.result.isDelete").value(false));
    }

    @Test
    @DisplayName("카테고리 조회 - 카테고리를 조회한다.")
    void getCategoriesTest() throws Exception {
        // given & when
        ResultActions response = mockMvc.perform(get("/category")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].parentId").value(category.getParentId()))
                .andExpect(jsonPath("$.result[0].depth").value(category.getDepth()))
                .andExpect(jsonPath("$.result[0].sort").value(category.getSort()))
                .andExpect(jsonPath("$.result[0].categoryName").value(category.getCategoryName()))
                .andExpect(jsonPath("$.result[0].isDelete").value(category.getIsDelete()));
    }

    @Test
    @DisplayName("케테고리 수정 - 카테고리를 수정한다.")
    void modifyCategoryTest() throws Exception {
        // given
        int parentId = 1;
        int depth = 2;
        int sort = 2;
        String categoryName = "cloth";

        String requestBody = """
                {
                  "parentId": %d,
                  "depth": %d,
                  "sort": %d,
                  "categoryName": "%s"
                }
                """.formatted(parentId, depth, sort, categoryName);

        ResultActions response = mockMvc.perform(put("/category/{id}", category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.parentId").value(parentId))
                .andExpect(jsonPath("$.result.depth").value(depth))
                .andExpect(jsonPath("$.result.sort").value(sort))
                .andExpect(jsonPath("$.result.categoryName").value(categoryName));
    }

    @Test
    @DisplayName("케테고리 삭제 - 카테고리를 삭제한다.")
    void removeCategoryTest() throws Exception {
        // given & when
        ResultActions response = mockMvc.perform(delete("/category/{id}", category.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));

        Category deletedCategory = categoryRepository.findById(category.getId()).orElseThrow();
        assertThat(deletedCategory.getIsDelete())
                .as("카테고리 삭제는 isDelete == true")
                .isTrue();
    }
}
