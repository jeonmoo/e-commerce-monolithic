package com.test.ecommerce.domain.category.service;

import com.test.ecommerce.config.TestContainerBase;
import com.test.ecommerce.domain.category.dto.CategoryCreateRequest;
import com.test.ecommerce.domain.category.dto.CategoryModifyRequest;
import com.test.ecommerce.domain.category.dto.CategoryResponse;
import com.test.ecommerce.domain.category.entity.Category;
import com.test.ecommerce.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CategoryServiceTest extends TestContainerBase {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void clear() {
        // 메서드 실행 전 캐시 초기화
        redisTemplate.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushAll();
    }

    @Test
    @DisplayName("카테고리 생성 - 카테고리를 생성한다.")
    void createCategoryTest() {
        // given
        String categoryName = "테스트 카테고리";
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .parentId(0L)
                .depth(0)
                .sort(0)
                .categoryName(categoryName)
                .build();

        // when
        categoryService.createCategory(request);

        // then
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.getFirst();
        assertThat(categories).hasSize(1);
        assertThat(category.getCategoryName()).isEqualTo(categoryName);
    }

    @Test
    @DisplayName("카테고리 조회 - 카테고리를 조회한다.")
    void getCategoryTest() {
        // given
        long parentId = 0L;
        int depth = 0;
        int sort = 0;
        boolean isDelete = false;
        String categoryName = "테스트 카테고리";

        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .parentId(parentId)
                .depth(depth)
                .sort(sort)
                .categoryName(categoryName)
                .build();

        categoryService.createCategory(request);

        // when
        List<CategoryResponse> categories = categoryService.getCategories();
        CategoryResponse category = categories.getFirst();

        // then
        assertThat(categories).hasSize(1);
        assertThat(category.getParentId()).isEqualTo(parentId);
        assertThat(category.getDepth()).isEqualTo(depth);
        assertThat(category.getSort()).isEqualTo(sort);
        assertThat(category.getIsDelete()).isEqualTo(isDelete);
    }

    @Test
    @Disabled
    @DisplayName("카테고리 조회 캐시 - 카테고리를 조회시 캐시를 저장한다.")
    void getCategoryCacheTest() throws IOException {
        // given
        long parentId = 0L;
        int depth = 0;
        int sort = 0;
        boolean isDelete = false;
        String categoryName = "테스트 카테고리";

        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .parentId(parentId)
                .depth(depth)
                .sort(sort)
                .categoryName(categoryName)
                .build();

        categoryService.createCategory(request);

        // when
        categoryService.getCategories();

        // then
        String json = redisTemplate.opsForValue().get("categoryList::allCategories");
        ObjectMapper mapper = new ObjectMapper();
        CategoryResponse category = mapper.readValue(json, CategoryResponse.class);

        assertThat(category.getParentId()).isEqualTo(parentId);
        assertThat(category.getDepth()).isEqualTo(depth);
        assertThat(category.getSort()).isEqualTo(sort);
        assertThat(category.getIsDelete()).isEqualTo(isDelete);
        assertThat(category.getCategoryName()).isEqualTo(categoryName);
    }

    @Test
    @DisplayName("카테고리 수정 - 카테고리를 수정한다.")
    void modifyCategoryTest() {
        // given
        long parentId = 0L;
        int depth = 0;
        int sort = 0;
        String categoryName = "테스트 카테고리";

        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .parentId(parentId)
                .depth(depth)
                .sort(sort)
                .categoryName(categoryName)
                .build();

        categoryService.createCategory(request);
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.getFirst();

        // when
        long modifiedParentId = 1L;
        int modifiedDepth = 1;
        int modifiedSort = 1;
        String modifiedCategoryName = "신규 카테고리";
        long categoryId = category.getId();
        CategoryModifyRequest modifyRequest = CategoryModifyRequest.builder()
                .parentId(modifiedParentId)
                .depth(modifiedDepth)
                .sort(modifiedSort)
                .categoryName(modifiedCategoryName)
                .build();

        categoryService.modifyCategory(categoryId, modifyRequest);

        // then
        Category modifiedCategory = categoryRepository.findById(categoryId).orElseThrow();

        assertThat(modifiedCategory.getParentId()).isEqualTo(modifiedParentId);
        assertThat(modifiedCategory.getDepth()).isEqualTo(modifiedDepth);
        assertThat(modifiedCategory.getSort()).isEqualTo(modifiedSort);
        assertThat(modifiedCategory.getCategoryName()).isEqualTo(modifiedCategoryName);
    }

    @Test
    @DisplayName("카테고리 삭제 - 카테고리 isDelete를 true로 수정한다")
    void removeCategoryTest() {
        // given
        String categoryName = "테스트 카테고리";
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .parentId(0L)
                .depth(0)
                .sort(0)
                .categoryName(categoryName)
                .build();
        categoryService.createCategory(request);
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.getFirst();

        // when
        long deleteId = category.getId();
        categoryService.removeCategory(deleteId);

        // then
        Category removedCategory = categoryRepository.findById(deleteId).get();
        assertThat(removedCategory.getIsDelete()).isTrue();
    }
}
