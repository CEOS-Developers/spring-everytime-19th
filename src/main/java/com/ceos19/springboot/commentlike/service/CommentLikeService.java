package com.ceos19.springboot.commentlike.service;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.commentlike.domain.CommentLike;
import com.ceos19.springboot.commentlike.repository.CommentLikeRepository;
import com.ceos19.springboot.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
