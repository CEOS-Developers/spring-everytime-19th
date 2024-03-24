package com.ceos19.everytime.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.board.domain.Board;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.post.domain.Posts;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.board.dto.request.BoardPostsRequestDto;
import com.ceos19.everytime.post.dto.request.PostCreateRequestDto;
import com.ceos19.everytime.board.dto.response.BoardPostsResponseDto;
import com.ceos19.everytime.post.dto.response.PostResponseDto;
import com.ceos19.everytime.board.repository.BoardRepository;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.user.repository.UserRepository;

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
        final Board board = getBoard(request.boardId());
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
    public PostResponseDto getPost(final Long postId) {
        return postRepository.findById(postId)
                .map(post -> new PostResponseDto(post.getTitle(), post.getContent(), post.getWriterNickname(),
                        post.getBoard().getName()))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<BoardPostsResponseDto> getPosts(final BoardPostsRequestDto request) {
        final Board findBoard = getBoard(request.boardId());
        final Posts posts = new Posts(postRepository.findAllFetchJoin(findBoard));
        return posts.toResponseDto();
    }

    private Board getBoard(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
