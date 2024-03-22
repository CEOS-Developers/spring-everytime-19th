package com.ceos19.everytime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.CommentWriteRequestDto;
import com.ceos19.everytime.repository.CommentLikeRepository;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void writeComment(final CommentWriteRequestDto request) {
        final User writer = userRepository.findById(request.writerId())
                .orElseThrow(IllegalArgumentException::new);
        final Post post = postRepository.findById(request.postId())
                .orElseThrow(IllegalArgumentException::new);
        final Comment parentComment = findParent(request.parentCommentId());
        final Comment comment = Comment.createComment(request.content(), request.isAnonymous(), writer, post, parentComment);

        commentRepository.save(comment);
    }

    @Transactional
    public void delete(final Long commentId) {
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
        commentRepository.deleteById(commentId);
        commentLikeRepository.deleteAllByComment(comment);
    }

    private Comment findParent(final Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }
        return commentRepository.findById(parentCommentId)
                .orElse(null);
    }
}
