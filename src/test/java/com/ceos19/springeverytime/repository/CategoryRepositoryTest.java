package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeEach
    public void 테스트_셋업() {
        user1 = createUser("user1");
    }

    @Test
    public void 카테고리_생성_테스트() throws Exception {
        //given
        Category category = createCategory(user1);

        //when
        categoryRepository.save(category);

        //then
        Assertions.assertEquals(categoryRepository.findOne(category.getCategoryId()), category);
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
        Assertions.assertEquals(3, categoryRepository.findOne(freeCategory.getCategoryId()).getPosts().size());

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
         * Hibernate:
         *     insert
         *     into
         *         user
         *         (admission_year,create_date,email,is_enrolled,major,name,nickname,pw,id)
         *     values
         *         (?,?,?,?,?,?,?,?,?)
         * Hibernate:
         *     insert
         *     into
         *         category
         *         (create_date,description,manager_id,name,category_id)
         *     values
         *         (?,?,?,?,?)
         * Hibernate:
         *     insert
         *     into
         *         post
         *         (author_id,category_id,content,create_date,is_anonymous,modify_date,title,post_id)
         *     values
         *         (?,?,?,?,?,?,?,?)
         * Hibernate:
         *     insert
         *     into
         *         post
         *         (author_id,category_id,content,create_date,is_anonymous,modify_date,title,post_id)
         *     values
         *         (?,?,?,?,?,?,?,?)
         * Hibernate:
         *     insert
         *     into
         *         post
         *         (author_id,category_id,content,create_date,is_anonymous,modify_date,title,post_id)
         *     values
         *         (?,?,?,?,?,?,?,?)
         * Hibernate:
         *     select
         *         c1_0.category_id,
         *         c1_0.create_date,
         *         c1_0.description,
         *         m1_0.id,
         *         m1_0.admission_year,
         *         m1_0.create_date,
         *         m1_0.email,
         *         m1_0.is_enrolled,
         *         m1_0.major,
         *         m1_0.name,
         *         m1_0.nickname,
         *         m1_0.pw,
         *         c1_0.name
         *     from
         *         category c1_0
         *     left join
         *         user m1_0
         *             on m1_0.id=c1_0.manager_id
         *     where
         *         c1_0.category_id=?
         * Hibernate:
         *     select
         *         p1_0.category_id,
         *         p1_0.post_id,
         *         a1_0.id,
         *         a1_0.admission_year,
         *         a1_0.create_date,
         *         a1_0.email,
         *         a1_0.is_enrolled,
         *         a1_0.major,
         *         a1_0.name,
         *         a1_0.nickname,
         *         a1_0.pw,
         *         p1_0.content,
         *         p1_0.create_date,
         *         p1_0.is_anonymous,
         *         p1_0.modify_date,
         *         p1_0.title
         *     from
         *         post p1_0
         *     left join
         *         user a1_0
         *             on a1_0.id=p1_0.author_id
         *     where
         *         p1_0.category_id=?
         * */
    }

    private User createUser(String id) {
        User user = new User(
                id,
                "1234",
                "nickname",
                "kim",
                "computer",
                "20",
                "test@example.com",
                true,
                new Date());

        userRepository.save(user);
        return user;
    }

    private Category createCategory(User manager) {
        Category category = new Category("자유게시판", "자유롭게 이야기 해봐요", manager);
        categoryRepository.save(category);
        return category;
    }

}
