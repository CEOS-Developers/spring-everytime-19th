package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.dto.CommentDTO;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.exception.NotFoundException;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public Comment getCommentById(long commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));
    }


    public Comment addNewComment(CommentDTO commentDTO, long postId) {

        //게시글 조회하여 존재 여부 확인
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));

        //댓글 엔티티 생성
        Comment comment = Comment.builder().contents(commentDTO.contents()).build();

        //댓글 엔티티를 DB에 저장
        Comment savedComment = commentRepository.save(comment);

        return savedComment;
    }

    public List<CommentDTO> getComments(Long postId) {
        return commentRepository.findByPost_postId(postId)
                .stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }

}
