package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    User user1, user2;
    Category category;
    Post post;

    @BeforeEach
    void 테스트_셋업() {
        user1 = new User(
                "test",
                "adsfbsa234@ad",
                "nicnname",
                "kim",
                "computer",
                "20",
                "test@exmaple.com",
                true
        );

        user2 = new User(
                "test2",
                "adsfbsa234@ad",
                "nickname2",
                "kwon",
                "data",
                "21",
                "test2@exmaple.com",
                true
        );

        category = new Category("자유게시판", "", user1);

        post = new Post(
                "송프언 과제 질문이요",
                "프롤로그 진짜 어케해요..?",
                true,
                new Date(), new Date(), user2, category);
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void 댓글_작성_테스트() {
        // given
        Comment comment = new Comment("화이팅", true, user2, post);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Comment comment1 = commentService.createComment(comment);

        // then
        Assertions.assertEquals(comment, comment1);
    }
}
