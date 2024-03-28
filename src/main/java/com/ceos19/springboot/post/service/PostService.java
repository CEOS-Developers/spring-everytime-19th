package com.ceos19.springboot.post.service;

import com.ceos19.springboot.board.entity.Board;
import com.ceos19.springboot.board.repository.BoardRepository;
import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.ResponseUtils;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.post.dto.PostRequestDto;
import com.ceos19.springboot.post.dto.PostResponseDto;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    // post 한개 가져오기
    @Transactional
    public ApiResponseDto<PostResponseDto> getOnePost(Long postId)
    {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        PostResponseDto postResponseDto =  new PostResponseDto(post.getPostId(),post.getTitle(),post.getAnonymous(), post.getLikes(), post.getView());
        return ResponseUtils.ok(postResponseDto);
    }
    // post 만들기
    @Transactional
    public ApiResponseDto<SuccessResponse> createPost(
            Long boardId, PostRequestDto postRequestDto
    )
    {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new RestApiException(ErrorType.NOT_FOUND)
        );
        Post post = new Post(board ,postRequestDto.getTitle(),  postRequestDto.getContent(),postRequestDto.getAnonymous());
        postRepository.save(post);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK,"Post create success"));
    }
}
