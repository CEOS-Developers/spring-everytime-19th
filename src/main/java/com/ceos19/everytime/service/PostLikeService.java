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
        final Post post = postRepository.findById(request.postId())
                .orElseThrow(IllegalArgumentException::new);
        final User user = userRepository.findById(request.userId())
                .orElseThrow(IllegalArgumentException::new);
        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new IllegalArgumentException();
        }
        final PostLike postLike = new PostLike(user, post);
        post.increaseLikeNumber();

        postLikeRepository.save(postLike);
    }
}
