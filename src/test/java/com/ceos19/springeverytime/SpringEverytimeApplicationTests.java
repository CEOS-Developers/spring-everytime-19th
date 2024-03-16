package com.ceos19.springeverytime;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
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
    @Rollback(value = false)
    void 채팅_메세지_작성_테스트() {

        // given
        User member1 = new User(
                "test",
                "1234",
                "nickname",
                "kim",
                "computer",
                "20",
                "test@example.com",
                true,
                new Date());

        Category freeCategory = new Category("자유게시판", "자유롭게 이야기 해봐요", member1);

        Post post1 = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), member1, freeCategory);
        Post post2 = new Post("두번째 글", "두번째 글입니다.", true, new Date(), new Date(), member1, freeCategory);
        Post post3 = new Post("세번째 글", "세번째 글입니다.", true, new Date(), new Date(), member1, freeCategory);

        em.persist(member1);
        em.persist(freeCategory);
        em.persist(post1);
        em.persist(post2);
        em.persist(post3);

        // when
        freeCategory.addPost(post1);
        freeCategory.addPost(post2);
        freeCategory.addPost(post3);

        // then
        Category testCategory = em.find(Category.class, 1L);
        Assertions.assertEquals(testCategory.getPosts().size(), 3);
    }

}
