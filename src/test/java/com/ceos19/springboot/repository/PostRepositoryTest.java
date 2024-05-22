//package com.ceos19.springboot.repository;
//
//import com.ceos19.springboot.post.domain.Post;
//import com.ceos19.springboot.post.repository.PostRepository;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Transactional
//public class PostRepositoryTest {
//    @Autowired
//    private PostRepository postRepository;
//
//    @Test
//    public void postSaveTest() throws Exception {
//        //given
//        Post post = Post.builder().build();
//        //when
//        postRepository.save(post);
//        Post findPost = postRepository.findById(post.getPostId()).orElseThrow(() -> new EntityNotFoundException("없음"));
//        //then
//        Assertions.assertThat(post.getPostId()).isEqualTo(findPost.getPostId());
//    }
//}
