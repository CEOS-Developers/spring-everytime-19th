package com.ceos19.springeverytime.domain.like.service;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_USER_ID;
import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_POST_ID;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


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
    public CommentLike createCommentLike(Comment comment, User user) {
        CommentLike commentLike = new CommentLike(user, comment);
        return likeRepository.save(commentLike);
    }

    @Transactional
    public void removeCommentLike(Comment comment, User user) {
        Optional<CommentLike> findCommentLike = likeRepository.findCommentLikeByCommentAndUser(comment, user);
        if (findCommentLike.isEmpty()) {
            throw new IllegalArgumentException("해당하는 좋아요 데이터가 없습니다.");
        }

        likeRepository.delete(findCommentLike.get());
    }
}
