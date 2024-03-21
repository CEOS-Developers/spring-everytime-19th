package com.ceos19.springboot.domain;

import com.ceos19.springboot.repository.CommentRepository;
import com.ceos19.springboot.repository.PostRepository;
import com.ceos19.springboot.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("댓글 작성 기능 테스트")
    public void createComment() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();

        Users commentUser = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉네임")
                .username("홍길동")
                .password("비번")
                .build();
        Users savePostUser = userRepository.save(user);
        Users saveCommentUser = userRepository.save(commentUser);

        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(user)
                .build();
        Posts savePost = postRepository.save(post);

        Comment comment = Comment.builder()
                .user(commentUser)
                .post(post)
                .content("댓글 내용")
                .build();
        //when
        Comment saveComment = commentRepository.save(comment);
        Comment findComment = commentRepository.findByContent(saveComment.getContent());
        //then
        Assertions.assertThat(saveComment.getContent()).isEqualTo(findComment.getContent());
    }

    @Test
    @DisplayName("대댓글 기능 테스트")
    public void childCommentTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        Users commentUser = Users.builder()
                .university("홍익대")
                .email("댓글을 달 사용자")
                .loginId("아이디")
                .nickname("닉네임")
                .username("홍길동")
                .password("비번")
                .build();
        Users savePostUser = userRepository.save(user);
        Users saveCommentUser = userRepository.save(commentUser);

        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(user)
                .build();
        Posts savePost = postRepository.save(post);

        Comment comment = Comment.builder()
                .user(commentUser)
                .post(post)
                .content("댓글 내용")
                .build();
        Comment saveComment = commentRepository.save(comment);

        Comment childComment = Comment.builder()
                .post(post)
                .parent(comment)
                .user(commentUser)
                .content("대댓글 내용")
                .build();
        //when
        Comment saveChildComment = commentRepository.save(childComment);
        comment.addChildComment(saveChildComment);
        //then
        Assertions.assertThat(saveComment.getChildren().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 삭제 기능 테스트")
    public void deleteCommentTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();

        Users commentUser = Users.builder()
                .university("홍익대")
                .email("댓글을 작성할 사용자")
                .loginId("아이디")
                .nickname("닉네임")
                .username("홍길동")
                .password("비번")
                .build();
        Users savePostUser = userRepository.save(user);
        Users saveCommentUser = userRepository.save(commentUser);

        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글 내용")
                .imageUrl("image url")
                .title("게시글 제목")
                .likes(0)
                .user(savePostUser)
                .build();
        Posts savePost = postRepository.save(post);

        Comment comment = Comment.builder()
                .user(commentUser)
                .post(savePost)
                .content("댓글 내용")
                .build();
        Comment saveComment = commentRepository.save(comment);
        savePost.addComment(saveComment);
        System.out.println("savePost.getComments() = " + savePost.getComments());
        //when
        commentRepository.delete(saveComment);
        savePost.removeComment(saveComment);
        //then
        System.out.println("savePost.getComments() = " + savePost.getComments());
    }
}