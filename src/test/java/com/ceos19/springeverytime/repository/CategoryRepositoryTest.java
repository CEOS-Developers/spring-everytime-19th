package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

//    @BeforeEach
//    public void 테스트_셋업() {
//        user1 = createUser("user1");
//    }

    @Test
    public void 카테고리_생성_테스트() throws Exception {
        //given
        Category category = createCategory(user1);

        //when
        categoryRepository.save(category);
        Category findCategory = categoryRepository.findById(category.getCategoryId()).get();

        //then
        Assertions.assertThat(category.getCategoryId()).isEqualTo(findCategory.getCategoryId());
    }

    @Test
    void 카테고리_하나에_여러_글_작성_테스트() {

        // given
        Category freeCategory = createCategory(user1);

        // when
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user1, freeCategory);
        Post post2 = new Post("두번째 글", "두번째 글입니다.", true, new Date(), new Date(), user1, freeCategory);
        Post post3 = new Post("세번째 글", "세번째 글입니다.", true, new Date(), new Date(), user1, freeCategory);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        em.flush();
        em.clear();

        // then
//        Assertions.assertEquals(3, categoryRepository.findOne(freeCategory.getCategoryId()).getPosts().size());
    }

//    private User createUser(String id) {
////        User user = new User(
////                id,
////                "1234",
////                "nickname",
////                "kim",
////                "computer",
////                "20",
////                "test@example.com",
////                true,
////                new Date());
//
//        userRepository.save(user);
//        return user;
//    }

    private Category createCategory(User manager) {
        Category category = new Category("자유게시판", "자유롭게 이야기 해봐요", manager);
        categoryRepository.save(category);
        return category;
    }

}
