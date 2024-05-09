package com.ceos19.springboot.comment.service;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.common.code.ErrorCode;
import com.ceos19.springboot.global.exception.BusinessExceptionHandler;
import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.post.service.PostService;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    // 댓글 저장 기능
    @Transactional
    public Comment saveComment(CommentRequestDto commentRequestDto, Post post) {
        Users user = userService.findUser(commentRequestDto.getUserId());
        Comment createdComment = Comment.builder()
                .content(commentRequestDto.getContent())
                .post(post)
                .user(user)
                .build();
        return commentRepository.save(createdComment);
    }

    // 대댓글 기능
    @Transactional
    public void setChildComment(Comment parentComment, Comment childComment) {
        parentComment.addChildComment(childComment);
        childComment.setParent(parentComment);
    }

    //댓글 조회
    public List<Comment> retreiveComment(Long postId) {
        return commentRepository.findByPost(postService.retreiveOnePost(postId));
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public boolean isOwner(Long commentId, Users user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        return user.equals(comment.getUser());
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
    }
}
