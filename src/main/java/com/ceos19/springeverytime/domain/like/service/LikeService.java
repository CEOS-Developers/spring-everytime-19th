package com.ceos19.springeverytime.domain.like.service;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.comment.repository.CommentRepository;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.like.domain.CommentLike;
import com.ceos19.springeverytime.domain.like.domain.PostLike;
import com.ceos19.springeverytime.domain.like.repository.LikeRepository;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public void updatePostLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        likeRepository.findPostLikeByPostIdAndUserId(postId, userId).ifPresentOrElse(
                likeRepository::delete,
                () -> {
                    final PostLike postLike = new PostLike(user, post);
                    likeRepository.save(postLike);
                }
        );
    }

    @Transactional
    public void updateCommentLike(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_COMMENT_ID));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        likeRepository.findCommentLikeByCommentIdAndUserId(commentId, userId).ifPresentOrElse(
                likeRepository::delete,
                () -> {
                    final CommentLike commentLike = new CommentLike(user, comment);
                    likeRepository.save(commentLike);
                }
        );
    }
}
