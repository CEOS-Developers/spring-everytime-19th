package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
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

    @Test
    public void 카테고리_생성_테스트() throws Exception {
        //given
        User user1 = createUser("user1");
        Category category = createCategory(user1);
        em.persist(user1);

        //when
        categoryRepository.save(category);

        //then
        Assertions.assertEquals(em.find(Category.class, category.getCategoryId()), category);
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
        return freeCategory;
    }

}
