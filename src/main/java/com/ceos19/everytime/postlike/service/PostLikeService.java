package com.ceos19.everytime.postlike.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.global.exception.NotFoundException;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.postlike.domain.PostLike;
import com.ceos19.everytime.postlike.dto.request.PostLikeRequestDto;
import com.ceos19.everytime.postlike.repository.PostLikeRepository;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(final Long postId, final PostLikeRequestDto request) {
        final Post post = getPost(postId);
        final User user = getUser(request);
        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new NotFoundException(String.format("PostLike not found: %d", postId));
        }
        final PostLike postLike = new PostLike(user, post);
        post.increaseLikeNumber();

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void cancelLike(final Long postId, final PostLikeRequestDto request) {
        final Post post = getPost(postId);
        final User user = getUser(request);
        final PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new NotFoundException(String.format("PostLike not found: %d", postId)));
        post.decreaseLikeNumber();

        postLikeRepository.delete(postLike);
    }
    private Post getPost(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(String.format("Post not found: %d", postId)));
    }

    private User getUser(final PostLikeRequestDto request) {
        return userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(String.format("User not found: %d", request.userId())));
    }
}
