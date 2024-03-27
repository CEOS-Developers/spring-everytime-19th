package com.ceos19.springeverytime.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.ceos19.springeverytime.comment.domain.Comment;
import com.ceos19.springeverytime.comment.dto.CommentDto;
import com.ceos19.springeverytime.comment.repository.CommentRepository;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.service.PostService;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @MockBean
    private CommentRepository commentRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setup() {
        User user = User.builder()
                .id(1L)
                .build();
        Post post = Post.builder()
                .id(1L)
                .build();

        when(postService.getPost(1L)).thenReturn(post);
        when(userService.getUser(1L)).thenReturn(user);
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void createComment() {
        //given
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .postId(1L)
                .userId(1L)
                .content("test")
                .build();

        Comment saveComment = commentDto.toEntity(user,post);
        //when
        Long saveId = commentService.createComment(commentDto);
        //then
        assertEquals(saveComment.getId(), commentDto.getId());
    }

    @Test
    void getComment() {
    }

    @Test
    void deleteComment() {
    }
}