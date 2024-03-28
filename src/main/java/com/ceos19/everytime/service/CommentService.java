package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    public Long addComment(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 댓글입니다");
        }
        return optionalComment.get();
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentReplies(Long commentId) {
        return commentRepository.findByParentCommentId(commentId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByCommenterId(Long commenterId) {
        return commentRepository.findByCommenterId(commenterId);
    }


    public void removeComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 댓글입니다");
        }

        List<Comment> replies = commentRepository.findByParentCommentId(commentId);
        // 연관관계 제거
        for (Comment reply : replies) {
            reply.removeParentComment();
        }
        commentRepository.deleteById(commentId);
    }
}
