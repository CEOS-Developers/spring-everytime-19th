package com.ceos19.springboot.post.service;

import com.ceos19.springboot.board.entity.Board;
import com.ceos19.springboot.board.repository.BoardRepository;
import com.ceos19.springboot.comment.dto.CommentResponseDto;
import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.ResponseUtils;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.post.dto.PostRequestDto;
import com.ceos19.springboot.post.dto.PostResponseDto;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.reply.dto.ReplyResponseDto;
import com.ceos19.springboot.reply.entity.Reply;
import com.ceos19.springboot.reply.repository.ReplyRepository;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    // post 한개 가져오기
    @Transactional
    public ApiResponseDto<PostResponseDto> getOnePost(Long postId,UserDetails loginUser )
    {
        User loginuser = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        List<Comment> commentList = commentRepository.findByPostId(post);
        List<CommentResponseDto> commentResponseDtoList = commentList.stream()
                .map(comment -> {
                    // 해당 댓글에 대한 답글 목록을 가져오기
                    List<Reply> replyList = replyRepository.findAllByComment(comment);

                    // 답글 목록을 ReplyResponseDto 리스트로 매핑
                    List<ReplyResponseDto> replyResponseDtoList = replyList.stream()
                            .map(reply -> new ReplyResponseDto(
                                    reply.getComment().getCommentId(),
                                    reply.getContent(),
                                    reply.getLikes()
                            ))
                            .collect(Collectors.toList());

                    // CommentResponseDto 객체 생성
                    return new CommentResponseDto(
                            comment.getContent(),
                            comment.getUser().getUsername(),
                            comment.getContentLike(),
                            replyResponseDtoList
                    );
                })
                .collect(Collectors.toList());


        PostResponseDto postResponseDto =  new PostResponseDto(post.getPostId(),post.getTitle(),post.getAnonymous(), post.getLikes(), post.getView(), commentResponseDtoList);
        return ResponseUtils.ok(postResponseDto);
    }
    // post 만들기
    @Transactional
    public ApiResponseDto<SuccessResponse> createPost(
            PostRequestDto postRequestDto, UserDetails loginUser
    )
    {
        Board board = boardRepository.findById(postRequestDto.getBoardId()).orElseThrow(
                ()-> new RestApiException(ErrorType.NOT_FOUND)
        );
        User loginuser = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Post post = new Post(loginuser, board ,postRequestDto.getTitle(),  postRequestDto.getContent(),postRequestDto.getAnonymous());
        postRepository.save(post);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK,"Post create success"));
    }
}
