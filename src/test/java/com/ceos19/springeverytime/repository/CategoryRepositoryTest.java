package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.category.repository.CategoryRepository;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class CategoryRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    User user1;

    @BeforeEach
    public void 테스트_셋업() {
        user1 = EntityGenerator.generateUser("user1");
        userRepository.save(user1);
    }

    @Test
    public void 카테고리_생성_테스트() throws Exception {
        //given
        Category category = EntityGenerator.generateCategory(user1);
        categoryRepository.save(category);

        //when
        Category saveCategory = categoryRepository.save(category);

        //then
        Assertions.assertThat(category.getCategoryId()).isEqualTo(saveCategory.getCategoryId());
    }

    @Test
    void 카테고리_하나에_여러_글_작성_테스트() {

        // given
        Category freeCategory = EntityGenerator.generateCategory(user1);
        categoryRepository.save(freeCategory);

        // when
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, user1, freeCategory);
        Post post2 = new Post("두번째 글", "두번째 글입니다.", true, user1, freeCategory);
        Post post3 = new Post("세번째 글", "세번째 글입니다.", true, user1, freeCategory);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        em.flush();
        em.clear();

        // then
        Assertions.assertThat(3)
                .isEqualTo(categoryRepository.findById(freeCategory.getCategoryId()).get().getPosts().size());
    }

    @Test
    @DisplayName("카테고리 조회 테스트")
    void 카테고리_조회_테스트() {
        // given
        Category freeCategory = EntityGenerator.generateCategory(user1);
        categoryRepository.save(freeCategory);

        // when
        Optional<Category> findCategory = categoryRepository.findById(freeCategory.getCategoryId());

        // then
        Assertions.assertThat(findCategory.get()).isSameAs(freeCategory);
    }
}
