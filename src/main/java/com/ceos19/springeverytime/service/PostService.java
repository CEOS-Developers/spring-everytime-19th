package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post findOne(Long postId) {
        return postRepository.findOne(postId);
    }

    @Transactional
    public Long save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public void remove(Post post) {
        postRepository.remove(post);
    }
}
