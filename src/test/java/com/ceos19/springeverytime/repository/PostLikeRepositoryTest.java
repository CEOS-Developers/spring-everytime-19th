package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.PostLike;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostLikeRepositoryTest {

    @Autowired PostLikeRepository postLikeRepository;
    @Autowired EntityManager em;

    @Test
    void findOne() {
        //given
        PostLike postLike1 = new PostLike();
        PostLike postLike2 = new PostLike();
        //when
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);
        //then
        assertEquals(postLike1, postLikeRepository.findOne(postLike1.getId()));
    }

    @Test
    void findByPost() {
        //given
        Post targetPost = new Post();
        Post nonTargetPost = new Post();
        em.persist(targetPost);
        em.persist(nonTargetPost);
        PostLike postLike1 = new PostLike();
        PostLike postLike2 = new PostLike();
        PostLike postLike3 = new PostLike();

        //when
        postLike1.setPost(targetPost);
        postLike2.setPost(targetPost);

        postLike3.setPost(nonTargetPost);
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);
        postLikeRepository.save(postLike3);

        List<PostLike> AllPostLikes = postLikeRepository.findByPost(targetPost.getId());

        //then
        assertEquals(2, AllPostLikes.size());
    }

    @Test
    void findByUser() {
        //given
        User targetUser = new User();
        User nonTargetUser = new User();
        em.persist(targetUser);
        em.persist(nonTargetUser);
        PostLike postLike1 = new PostLike();
        PostLike postLike2 = new PostLike();
        PostLike postLike3 = new PostLike();

        //when
        postLike1.setUser(targetUser);
        postLike2.setUser(targetUser);

        postLike3.setUser(nonTargetUser);
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);
        postLikeRepository.save(postLike3);

        List<PostLike> AllPostLikes = postLikeRepository.findByUser(targetUser.getId());

        //then
        assertEquals(2, AllPostLikes.size());
    }
}