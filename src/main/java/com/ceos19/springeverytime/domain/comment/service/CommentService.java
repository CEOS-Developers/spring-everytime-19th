package com.ceos19.springeverytime.domain.comment.service;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.comment.dto.request.CommentCreateRequest;
import com.ceos19.springeverytime.domain.comment.repository.CommentRepository;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Comment save(final Long postId, final Long userId, final CommentCreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));

        Comment newComment = Comment
                .builder()
                .content(request.getContent())
                .isAnonymous(request.isAnonymous())
                .post(post)
                .author(author)
                .build();
        return commentRepository.save(newComment);
    }

    public void deleteComment(final Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void validateCommentByUser(final Long userId, final Long commentId) {
        if (!commentRepository.existsByAuthorUserIdAndCommentId(userId, commentId)) {
            throw new BadRequestException(NOT_FOUND_COMMENT_WITH_MEMBER);
        }
    }
}
