package com.ceos19.everytime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.dto.request.PostRequestDto;
import com.ceos19.everytime.dto.response.PostResponseDto;
import com.ceos19.everytime.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public PostResponseDto getPost(final PostRequestDto request) {
        return postRepository.findById(request.id())
                .map(post -> new PostResponseDto(post.getTitle(), post.getContent(), post.getWriterNickname(),
                        post.getBoard().getName()))
                .orElseThrow(IllegalArgumentException::new);
    }
}
