package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Comment;
import com.ceos19.springboot.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    // 댓글 저장 기능
    @Transactional
    public Long saveComment(Comment comment) {
        Comment saveComment = commentRepository.save(comment);
        return saveComment.getCommentId();
    }

    // 대댓글 기능
    @Transactional
    public void setChildComment(Comment parentComment, Comment childComment) {
        parentComment.addChildComment(childComment);
        childComment.setParent(parentComment);
    }
}
