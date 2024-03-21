package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.domain.like.CommentLike;
import com.ceos19.springeverytime.domain.like.PostLike;
import com.ceos19.springeverytime.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void createPostLike(Post post, User user) {
        PostLike postLike = new PostLike(user, new Date(), post);
        likeRepository.save(postLike);
    }

    @Transactional
    public void createCommentLike(Comment comment, User user) {
        CommentLike commentLike = new CommentLike(user, new Date(), comment);
        likeRepository.save(commentLike);
    }

    @Transactional
    public void removePostLike() {}

    @Transactional
    public void removeCommentLike() {}
}
