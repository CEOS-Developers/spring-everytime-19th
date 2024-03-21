package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post findOne(Long postId) {
        return postRepository.findOne(postId);
    }

    public Long save(Post post) {
        return postRepository.save(post);
    }
}
