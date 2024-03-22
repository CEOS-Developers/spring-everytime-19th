package com.ceos19.springeverytime.comment.service;

import com.ceos19.springeverytime.comment.domain.Comment;
import com.ceos19.springeverytime.comment.dto.CommentDto;
import com.ceos19.springeverytime.comment.repository.CommentRepository;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.service.PostService;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private UserService userService;
    private PostService postService;

    public void createComment(CommentDto commentDto){
        User user = userService.getUser(commentDto.getUserId());
        Post post = postService.getPost(commentDto.getPostId());

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

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }
}
