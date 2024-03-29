package com.ceos19.springeverytime.domain.like.service;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.like.domain.CommentLike;
import com.ceos19.springeverytime.domain.like.domain.PostLike;
import com.ceos19.springeverytime.domain.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public PostLike createPostLike(Post post, User user) {
        PostLike postLike = new PostLike(user, post);
        return likeRepository.save(postLike);
    }

    @Transactional
    public CommentLike createCommentLike(Comment comment, User user) {
        CommentLike commentLike = new CommentLike(user, comment);
        return likeRepository.save(commentLike);
    }

    @Transactional
    public void removePostLike(Post post, User user) {
        Optional<PostLike> findPostLike = likeRepository.findPostLikeByPostAndUser(post, user);
        if (findPostLike.isEmpty()) {
            throw new IllegalArgumentException("해당하는 좋아요 데이터가 없습니다.");
        }

        likeRepository.delete(findPostLike.get());
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
