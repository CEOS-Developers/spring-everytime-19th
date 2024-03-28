package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post findById(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isPresent()) return findPost.get();
        throw new IllegalArgumentException("존재하지 않는 Post ID 입니다.");
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public void remove(Post post) {
        postRepository.delete(post);
    }
}
