package com.ceos19.everytime.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.comment.dto.request.CommentWriteRequestDto;
import com.ceos19.everytime.commentlike.repository.CommentLikeRepository;
import com.ceos19.everytime.comment.repository.CommentRepository;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.user.repository.UserRepository;

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
                .orElseThrow(() -> new IllegalArgumentException(String.format("User not found: %d", request.writerId())));
        final Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Post not found: %d", request.postId())));
        final Comment parentComment = findParent(request.parentCommentId());
        final Comment comment = Comment.createComment(request.content(), request.isAnonymous(), writer, post, parentComment);

        commentRepository.save(comment);
    }

    @Transactional
    public void delete(final Long commentId) {
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Comment not found: %d", commentId)));
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
