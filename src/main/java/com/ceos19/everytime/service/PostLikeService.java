package com.ceos19.everytime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.PostLikeRequestDto;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void like(final PostLikeRequestDto request) {
        final Post post = getPost(request);
        final User user = getUser(request);
        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new IllegalArgumentException();
        }
        final PostLike postLike = new PostLike(user, post);
        post.increaseLikeNumber();

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void cancelLike(final PostLikeRequestDto request) {
        final Post post = getPost(request);
        final User user = getUser(request);
        final PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(IllegalArgumentException::new);
        post.decreaseLikeNumber();

        postLikeRepository.delete(postLike);
    }

    private Post getPost(final PostLikeRequestDto request) {
        return postRepository.findById(request.postId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private User getUser(final PostLikeRequestDto request) {
        return userRepository.findById(request.userId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
