package com.ceos19.springboot.domain;

import com.ceos19.springboot.repository.PostRepository;
import com.ceos19.springboot.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostsTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Test
    public void createPost() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(user)
                .build();
        //when
        Users savedUser = userRepository.save(user);
        Posts savedPost = postRepository.save(post);
        Posts findPost = postRepository.findByTitle("게시글1 제목");
        //then
        Assertions.assertThat(savedPost.getContent()).isEqualTo(findPost.getContent());
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    public void searchPostsTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        Users saveUser = userRepository.save(user);
        //when
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Posts post2 = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글2 내용")
                .imageUrl("image url")
                .title("게시글2 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Posts post3 = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글3 내용")
                .imageUrl("image url")
                .title("게시글3 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Posts savePost1 = postRepository.save(post);
        Posts savePost2 = postRepository.save(post2);
        Posts savePost3 = postRepository.save(post3);
        //then
        System.out.println("saveUser = " + saveUser);
    }
    @Test
    @DisplayName("게시글에 좋아요 누르기 테스트")
    public void PostsLikeTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대학교")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비1번")
                .build();
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글2 내용입니다")
                .imageUrl("image url")
                .title("게시글2 제목입니다")
                .likes(0)
                .user(user)
                .build();
        Users savedUser = userRepository.save(user);
        Posts savedPost = postRepository.save(post);
        //when
        Posts changedPost = savedUser.pressLike(savedPost);
        postRepository.save(changedPost);
        //then
        Assertions.assertThat(savedPost.getLikes()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deletePost() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(user)
                .build();

        Users saveUser = userRepository.save(user);
        Posts savePost = postRepository.save(post);
        saveUser.addPosts(savePost);

        System.out.println("saveUser.getPosts() = " + saveUser.getPosts());
        //when
        postRepository.delete(savePost);
        saveUser.removePost(savePost);
        //then
        System.out.println("saveUser.getPosts() = " + saveUser.getPosts());
    }

    @Test
    @DisplayName("좋아요 취소하기 테스트")
    public void unlikePostsTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대학교")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비1번")
                .build();
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글2 내용입니다")
                .imageUrl("image url")
                .title("게시글2 제목입니다")
                .likes(0)
                .user(user)
                .build();
        Users savedUser = userRepository.save(user);
        Posts savedPost = postRepository.save(post);
        //when
        Posts pressLikePost = savedUser.pressLike(savedPost);
        Posts changedPost = savedUser.unlike(savedPost);
        //then
        Assertions.assertThat(changedPost.getLikes()).isEqualTo(0);
    }
}