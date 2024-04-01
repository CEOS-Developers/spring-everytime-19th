package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.dto.PostResponseDTO;
import com.ceos19.springboot.exception.ErrorCode;
import com.ceos19.springboot.exception.ErrorException;
import com.ceos19.springboot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponseDTO addPost(Post post) {
        if (postRepository.findById(post.getId()).isPresent()) {
            log.error("post failed : already exist post");
            throw new ErrorException(ErrorCode.DATA_ALREADY_EXIST, "post already exist");
        }

        postRepository.save(post);
        return PostResponseDTO.entityToDto(post);
    }

    public void removePost(Post post) {
        postRepository.delete(post);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> findByTitle(String title) {
        return postRepository.findByTitle(title);
    }
}
