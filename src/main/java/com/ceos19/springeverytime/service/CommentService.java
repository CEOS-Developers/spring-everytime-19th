package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Long createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void removeComment(Comment comment) {
        commentRepository.removeOne(comment);
    }
}
