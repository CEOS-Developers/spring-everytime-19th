package com.ceos19.springeverytime.postlike.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.postlike.domain.PostLike;
import com.ceos19.springeverytime.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostLikeRepositoryTest {

    @Autowired
    PostLikeRepository postLikeRepository;
    @Autowired EntityManager em;

    @Test
    void findOne() {
        //given
        PostLike postLike1 = PostLike.builder()
                .build();
        PostLike postLike2 = PostLike.builder()
                .build();
        //when
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);
        //then
        PostLike result = postLikeRepository.findById(postLike1.getId())
                .orElseThrow(IllegalStateException::new);

        assertEquals(postLike1, result);
    }

    @Test
    void findByPost() {
        //given
        Post targetPost = Post.builder().build();
        Post nonTargetPost = Post.builder().build();
        em.persist(targetPost);
        em.persist(nonTargetPost);

        PostLike postLike1 = PostLike.builder().
                post(targetPost)
                .build();
        PostLike postLike2 = PostLike.builder().
                post(targetPost)
                .build();
        PostLike postLike3 = PostLike.builder().
                post(nonTargetPost)
                .build();

        //when
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);
        postLikeRepository.save(postLike3);

        List<PostLike> AllPostLikes = postLikeRepository.findByPostId(targetPost.getId());

        //then
        assertEquals(2, AllPostLikes.size());
    }

    @Test
    void findByUser() {
        //given
        User targetUser = User.builder()
                .build();
        User nonTargetUser = User.builder()
                .build();
        em.persist(targetUser);
        em.persist(nonTargetUser);
        PostLike postLike1 = PostLike.builder()
                .user(targetUser)
                .build();
        PostLike postLike2 = PostLike.builder()
                .user(targetUser)
                .build();
        PostLike postLike3 = PostLike.builder()
                .user(nonTargetUser)
                .build();

        //when
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);
        postLikeRepository.save(postLike3);

        List<PostLike> AllPostLikes = postLikeRepository.findByUserId(targetUser.getId());

        //then
        assertEquals(2, AllPostLikes.size());
    }

    @Test
    void delete() {
        //given
        PostLike postLike = PostLike.builder()
                .build();
        //when
        postLikeRepository.save(postLike);
        postLikeRepository.delete(postLike);

        //then
        assertThrows(EntityNotFoundException.class, () -> postLikeRepository.findById(postLike.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found")));
    }
}