package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post addPost(final Post post) {
        return postRepository.save(post);
    }

    public void removePost(final Post post) {
        postRepository.delete(post);
    }

    public Post updatePost(final Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> findByTitle(final String title) {
        return postRepository.findByTitle(title);
    }
}
