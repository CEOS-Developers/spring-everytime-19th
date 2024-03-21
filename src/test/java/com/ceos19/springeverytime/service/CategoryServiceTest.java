package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    User user1, user2;

    @BeforeEach
    void 테스트_셋업() {
        user1 = new User(
            "test",
            "adsfbsa234@ad",
            "nicnname",
            "kim",
            "computer",
            "20",
            "test@exmaple.com",
            true
        );

        user2 = new User(
                "test2",
                "adsfbsa234@ad",
                "nickname2",
                "kwon",
                "data",
                "21",
                "test2@exmaple.com",
                true
        );
    }

    @Test
    @DisplayName("게시판 생성 테스트")
    void 게시판_생성_테스트() {
        // given
        Category category = new Category("자유게시판", "", user1);
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        // when
        Long newCategoryId = categoryService.create(category);

        // then
        assertThat(newCategoryId).isEqualTo(category.getCategoryId());
    }

    @Test
    @DisplayName("게시판 관리자 변경 테스트")
    void 게시판_관리자_변경_테스트() {
        // given
        Category category = new Category("자유게시판", "", user1);
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));

        // when
        Category findCategory = categoryService.findById(1L);
        findCategory.changeManager(user2);

        // then
        assertThat(categoryService.findById(category.getCategoryId()).getManager()).isEqualTo(user2);
    }
}
