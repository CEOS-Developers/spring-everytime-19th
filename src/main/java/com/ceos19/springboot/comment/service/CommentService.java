package com.ceos19.springboot.comment.service;

import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.dto.CommentResponseDto;
import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
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
    public void createComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        // 가정: 사용자 ID로 사용자를 찾아서 댓글의 작성자로 설정합니다.
        // comment.setUser(userRepository.findById(commentRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음")));
        comment.setPost(postRepository.findByPostId(commentRequestDto.getPostId()));
        // 가정: contentLike는 초기값으로 0을 설정합니다.
        comment.setContentLike(0);
        commentRepository.save(comment);
    }
}
