package com.ceos19.springboot.comment.service;

import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.dto.CommentResponseDto;
import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.ResponseUtils;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 작성하기
    public ApiResponseDto<SuccessResponse> createComment(UserDetails loginUser, CommentRequestDto commentRequestDto, Long postId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        Comment comment = new Comment(commentRequestDto.getContent(), user,  post);
        comment.setContentLike(0);
        commentRepository.save(comment);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "comment create success"));
    }
    public ApiResponseDto<SuccessResponse> deleteComment(UserDetails loginUser,  Long commentId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        commentRepository.delete(comment);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "comment delete success"));
    }
}
