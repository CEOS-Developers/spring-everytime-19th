package com.ceos19.everytime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.PostCreateRequestDto;
import com.ceos19.everytime.dto.request.PostRequestDto;
import com.ceos19.everytime.dto.response.PostResponseDto;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void createPost(final PostCreateRequestDto request) {
        final User writer = userRepository.findById(request.userId())
                .orElseThrow(IllegalArgumentException::new);
        final Board board = boardRepository.findById(request.boardId())
                .orElseThrow(IllegalArgumentException::new);
        final Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .isAnonymous(request.isAnonymous())
                .writer(writer)
                .board(board)
                .build();
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(final PostRequestDto request) {
        return postRepository.findById(request.id())
                .map(post -> new PostResponseDto(post.getTitle(), post.getContent(), post.getWriterNickname(),
                        post.getBoard().getName()))
                .orElseThrow(IllegalArgumentException::new);
    }
}
