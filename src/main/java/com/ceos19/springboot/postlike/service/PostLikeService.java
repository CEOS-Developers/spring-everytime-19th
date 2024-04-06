package com.ceos19.springboot.postlike.service;

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
import com.ceos19.springboot.postlike.entity.postLike;
import com.ceos19.springboot.postlike.repository.PostLikeRepository;
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
public class PostLikeService {
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    @Transactional
    public ApiResponseDto<SuccessResponse> postLikeCreate(UserDetails loginUser, Long postId)
    {
        User user1;
        user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        Optional<postLike> postLikeOptional = postLikeRepository.findByUserAndPost(user1,post);
        if (postLikeOptional.isPresent()) {
            throw new RestApiException(ErrorType.ALREADY_EXIST);
        }
        else{
            postLike postLike = new postLike(user1,post);
            postLikeRepository.save(postLike);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "commentLike Create Success"));
        }
    }
    @Transactional
    public ApiResponseDto<SuccessResponse> postLikeDelete(UserDetails loginUser, Long postId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND));

        Optional<postLike> postLikeOptional = postLikeRepository.findByUserAndPost(user, post);
        if (postLikeOptional.isPresent()) {
            postLike postLike = postLikeOptional.get();
            postLikeRepository.delete(postLike);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "Like deleted successfully."));
        } else {
            throw new RestApiException(ErrorType.NOT_FOUND);
        }
    }

}
