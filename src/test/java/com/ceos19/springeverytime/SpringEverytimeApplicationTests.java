package com.ceos19.springeverytime;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.domain.like.PostLike;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
@Transactional
class SpringEverytimeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    EntityManager em;

    @Test
    void 채팅_메세지_작성_테스트() {

        // given
        User member1 = createUser("user1");

        Category freeCategory = createCategory(member1);

        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), member1, freeCategory);
        Post post2 = new Post("두번째 글", "두번째 글입니다.", true, new Date(), new Date(), member1, freeCategory);
        Post post3 = new Post("세번째 글", "세번째 글입니다.", true, new Date(), new Date(), member1, freeCategory);

        em.persist(member1);
        em.persist(post1);
        em.persist(post2);
        em.persist(post3);

        // when
        freeCategory.addPost(post1);
        freeCategory.addPost(post2);
        freeCategory.addPost(post3);

        // then
        Category testCategory = em.find(Category.class, freeCategory.getCategoryId());
        Assertions.assertEquals(testCategory.getPosts().size(), 3);

        /** 위 테스트에 대한 쿼리
         * Hibernate:
         *     select
         *         next_val as id_val
         *     from
         *         category_seq for update
         * Hibernate:
         *     update
         *         category_seq
         *     set
         *         next_val= ?
         *     where
         *         next_val=?
         * Hibernate:
         *     select
         *         null,
         *         u1_0.admission_year,
         *         u1_0.create_date,
         *         u1_0.email,
         *         u1_0.is_enrolled,
         *         u1_0.major,
         *         u1_0.name,
         *         u1_0.nickname,
         *         u1_0.pw
         *     from
         *         user u1_0
         *     where
         *         u1_0.id=?
         * Hibernate:
         *     select
         *         next_val as id_val
         *     from
         *         post_seq for update
         * Hibernate:
         *     update
         *         post_seq
         *     set
         *         next_val= ?
         *     where
         *         next_val=?
         * Hibernate:
         *     select
         *         next_val as id_val
         *     from
         *         post_seq for update
         * Hibernate:
         *     update
         *         post_seq
         *     set
         *         next_val= ?
         *     where
         *         next_val=?
         * */
    }

    @Test
    @Rollback(value = false)
    public void 좋아요_생성_테스트() throws Exception {
        //given
        User user1 = createUser("user1");
        User user2 = createUser("user2");
        Category freeCategory = createCategory(user1);
        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), user1, freeCategory);
        em.persist(user1);
        em.persist(user2);
        em.persist(post1);

        PostLike like1 = new PostLike(user1, new Date(), post1);
        em.persist(like1);
        PostLike like2 = new PostLike(user2, new Date(), post1);
        em.persist(like2);

        //when
        post1.addLike(like1);
        post1.addLike(like2);

        //then
        Assertions.assertEquals(post1.getPostLikes().size(), 2);
    }

    private User createUser(String id) {
        return new User(
                id,
                "1234",
                "nickname",
                "kim",
                "computer",
                "20",
                "test@example.com",
                true,
                new Date());
    }

    private Category createCategory(User manager) {
        Category freeCategory = new Category("자유게시판", "자유롭게 이야기 해봐요", manager);
        em.persist(freeCategory);
        return freeCategory;
    }
}
