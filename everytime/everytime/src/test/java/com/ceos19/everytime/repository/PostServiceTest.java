package com.ceos19.everytime.repository;


import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.dto.PostDTO;
import com.ceos19.everytime.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class PostServiceTest {
    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    private Post post;
    private PostDTO postDTO;
    @BeforeEach
    public void setup(){
        post = Post.builder()
                .postId(1L)
                .title("This is the title.")
                .contents("This is the test.")
                .likeNum(3L)
                .build();
        postDTO = new PostDTO(post.getTitle(), post.getContents(),post.getLikeNum());
    }

    @Order(1)
    @DisplayName("게시글 저장 기능 테스트")
    @Test
    void savePostTest() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(post);

        //when
        Post testedResult = postService.savePost(postDTO);

        //Then
        assertThat(testedResult).isEqualTo(post);
    }

    @Order(2)
    @DisplayName("게시글 좋아요 추가 테스트")
    @Test
    void addLikeNumTest(){
        // given
        Long initialLikeNum = post.getLikeNum();
        Post updatedPost = Post.builder()
                .postId(1L)
                .title("This is the title.")
                .contents("This is the test.")
                .likeNum(initialLikeNum + 1) // 좋아요 수를 1 증가시킨 post 객체
                .build();
        given(postRepository.findById(post.getPostId())).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(updatedPost);

        // when
        Post testedResult2 = postService.addLikeToPost(post.getPostId());

        // then
        assertThat(testedResult2.getLikeNum()).isEqualTo(initialLikeNum + 1);
    }

    @Order(3)
    @DisplayName("게시글 좋아요 삭제 테스트")
    @Test
    void deleteLikeNumTest(){
        // given
        Long initialLikeNum = post.getLikeNum();
        Post updatedPost = Post.builder()
                .postId(1L)
                .title("This is the title.")
                .contents("This is the test.")
                .likeNum(initialLikeNum - 1) // 좋아요 수를 1 감소시킨 post 객체
                .build();
        given(postRepository.findById(post.getPostId())).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(updatedPost);

        // when
        Post testedResult3 = postService.deleteLikeNum(post.getPostId());

        // then
        assertThat(testedResult3.getLikeNum()).isEqualTo(initialLikeNum - 1);
    }
}