package com.ceos19.springeverytime.comment.service;

import com.ceos19.springeverytime.comment.domain.Comment;
import com.ceos19.springeverytime.comment.dto.CommentDto;
import com.ceos19.springeverytime.comment.repository.CommentRepository;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.repository.PostRepository;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createComment(CommentDto commentDto) throws NotFoundException {
        User user = userRepository.findUserById(commentDto.getUserId())
                .orElseThrow(NotFoundException::new);
        Post post = postRepository.findPostById(commentDto.getPostId())
                .orElseThrow(NotFoundException::new);

        if(commentDto.getParentCommentId() != null)
        {
            Comment parentComment = getComment(commentDto.getParentCommentId());
            commentRepository.save(commentDto.toEntity(user,post,parentComment));
        }
        else
        {
            commentRepository.save(commentDto.toEntity(user,post));
        }
    }

    public Comment getComment(Long commentId){
        return commentRepository.findCommentById(commentId)
                .orElseThrow(IllegalStateException::new);
    }

    public List<CommentDto>getCommentsByPost(Long postId){
        return commentRepository.findCommentsByPostId(postId).stream()
                .map(CommentDto::of)
                .toList();
    }

    @Transactional
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }
}
