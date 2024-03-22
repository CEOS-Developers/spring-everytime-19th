package com.ceos19.springboot.comment.service;

import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.dto.CommentResponseDto;
import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    public List<CommentResponseDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getContent(),
                        comment.getUser().getUsername(), // 가정: User 엔티티에 getUsername() 메서드가 있다고 가정합니다.
                        comment.getContentLike()
                ))
                .collect(Collectors.toList());
    }
    // 댓글 작성하기
    public void createComment(UserDetails loginUser, CommentRequestDto commentRequestDto, Long postId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        Comment comment = new Comment(commentRequestDto.getContent(), user,  post);
        comment.setContentLike(0);
        commentRepository.save(comment);
    }
}
