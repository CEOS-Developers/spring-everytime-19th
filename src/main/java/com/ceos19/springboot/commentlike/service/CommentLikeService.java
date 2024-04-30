package com.ceos19.springboot.commentlike.service;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.commentlike.domain.CommentLike;
import com.ceos19.springboot.commentlike.repository.CommentLikeRepository;
import com.ceos19.springboot.common.code.ErrorCode;
import com.ceos19.springboot.global.exception.BusinessExceptionHandler;
import com.ceos19.springboot.postlike.domain.PostLike;
import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void pressLike(Comment comment, Users user){
        comment.pressLike();
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .user(user)
                .build();
        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void cancelLike(Comment comment, Users user){
        comment.cancelLike();
        CommentLike commentLike = commentLikeRepository.findByComment(comment).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        commentLikeRepository.delete(commentLike);
    }

    public boolean alreadyLiked(Long userId, Long commentId) {
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
        if (findCommentLike.isPresent())
            return true;
        else
            return false;
    }
}
