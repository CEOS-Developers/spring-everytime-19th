package com.ceos19.everytime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.CommentLike;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.repository.CommentLikeRepository;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.UserRepository;
import com.ceos19.everytime.request.CommentLikeRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(final CommentLikeRequestDto request) {
        final Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(IllegalArgumentException::new);
        final User user = userRepository.findById(request.userId())
                .orElseThrow(IllegalArgumentException::new);
        if (commentLikeRepository.existsByCommentAndUser(comment, user)) {
            throw new IllegalArgumentException();
        }
        final CommentLike commentLike = new CommentLike(user, comment);
        comment.increaseLikeNumber();

        commentLikeRepository.save(commentLike);
    }
}
