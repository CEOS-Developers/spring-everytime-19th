package com.ceos19.springboot.commentlike.service;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.commentlike.entity.CommentLike;
import com.ceos19.springboot.commentlike.repository.CommentLikeRepository;
import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.ResponseUtils;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentLikeService {
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Transactional
    public ApiResponseDto<SuccessResponse> commentLikeCreate(UserDetails loginUser, Long commentId)
    {
        User user1;
        user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Comment>commentOptional = commentRepository.findById(commentId);
        Comment comment = commentOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        Optional<CommentLike> commentLikeOptional = commentLikeRepository.findByUserAndComment(user1, comment);
        if (commentLikeOptional.isPresent()) {
            throw new RestApiException(ErrorType.ALREADY_EXIST);
        }
        else{
            CommentLike commentLike = new CommentLike(user1,comment);
            commentLikeRepository.save(commentLike);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "commentLike Create Success"));
        }
    }
    @Transactional
    public ApiResponseDto<SuccessResponse> commentLikeDelete(UserDetails loginUser, Long commentId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Comment comment = commentOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND));

        Optional<CommentLike> commentLikeOptional = commentLikeRepository.findByUserAndComment(user, comment);
        if (commentLikeOptional.isPresent()) {
            CommentLike commentLike = commentLikeOptional.get();
            commentLikeRepository.delete(commentLike);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "Comment Like deleted successfully."));
        } else {
            throw new RestApiException(ErrorType.NOT_FOUND);
        }
    }
}


