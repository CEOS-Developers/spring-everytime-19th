package com.ceos19.everytime.commentlike.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.comment.repository.CommentRepository;
import com.ceos19.everytime.commentlike.domain.CommentLike;
import com.ceos19.everytime.commentlike.dto.request.CommentLikeRequestDto;
import com.ceos19.everytime.commentlike.repository.CommentLikeRepository;
import com.ceos19.everytime.global.exception.BadRequestException;
import com.ceos19.everytime.global.exception.ExceptionCode;
import com.ceos19.everytime.global.exception.NotFoundException;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(final CommentLikeRequestDto request) {
        final Comment comment = getComment(request);
        final User user = getUser(request);
        if (commentLikeRepository.existsByCommentAndUser(comment, user)) {
            throw new BadRequestException(ExceptionCode.ALREADY_EXIST_COMMENT_LIKE);
        }
        final CommentLike commentLike = new CommentLike(user, comment);
        comment.increaseLikeNumber();

        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void cancel(final CommentLikeRequestDto request) {
        final Comment comment = getComment(request);
        final User user = getUser(request);
        final CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_COMMENT_LIKE));
        comment.decreaseLikeNumber();

        commentLikeRepository.delete(commentLike);
    }

    private Comment getComment(final CommentLikeRequestDto request) {
        return commentRepository.findById(request.commentId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_COMMENT));
    }

    private User getUser(final CommentLikeRequestDto request) {
        return userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
    }
}
