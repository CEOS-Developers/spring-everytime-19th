package com.ceos19.everytime.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos19.everytime.board.domain.Board;
import com.ceos19.everytime.board.dto.request.BoardPostsRequestDto;
import com.ceos19.everytime.board.dto.response.BoardPostsResponseDto;
import com.ceos19.everytime.board.repository.BoardRepository;
import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.comment.repository.CommentRepository;
import com.ceos19.everytime.global.exception.ExceptionCode;
import com.ceos19.everytime.global.exception.NotFoundException;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.post.dto.request.PostCreateRequestDto;
import com.ceos19.everytime.post.dto.response.PostResponseDto;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createPost(final PostCreateRequestDto request) {
        final User writer = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
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

    public PostResponseDto getPost(final Long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
        final List<Comment> comments = commentRepository.findByPost(post);
        return PostResponseDto.of(post, comments);
    }

    public List<BoardPostsResponseDto> getPosts(final BoardPostsRequestDto request) {
        final Board findBoard = getBoard(request.boardId());
        final List<Post> posts = postRepository.findAllFetchJoin(findBoard);
        return posts.stream()
                .map(BoardPostsResponseDto::from)
                .toList();
    }

    private Board getBoard(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_BOARD));
    }
}
