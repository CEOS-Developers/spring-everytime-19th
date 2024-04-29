package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.AddCommentRequest;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Deprecated
     */
    public Long addComment(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    public Long addComment(AddCommentRequest request, Long postId) {
        User commenter = userRepository.findById(request.getCommenterId()).orElseThrow(() -> {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 Post의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });


        Comment comment = new Comment(request.getContent(), commenter, post, null);
        commentRepository.save(comment);
        return comment.getId();
    }

    public Long addReply(AddCommentRequest request, Long commentId) {
        User commenter = userRepository.findById(request.getCommenterId()).orElseThrow(() -> {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });

        Comment parentComment = commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 Comment의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "대댓글을 달 수 있는 댓글이 존재하지 않습니다");
        });

        Post post = parentComment.getPost();

        Comment comment = new Comment(request.getContent(), commenter, post, parentComment);
        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 댓글입니다");
        });
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByPostId(Long postId) {
        postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 Post의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });

        return commentRepository.findByPostId(postId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentReplies(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("에러 내용: 대댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 Comment의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 댓글입니다");
        });

        return commentRepository.findByParentCommentId(commentId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByCommenterId(Long commenterId) {
        userRepository.findById(commenterId).orElseThrow(() -> {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
        return commentRepository.findByCommenterId(commenterId);
    }


    public void removeComment(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 댓글입니다");
        });

        List<Comment> replies = commentRepository.findByParentCommentId(commentId);
        // 연관관계 제거
        for (Comment reply : replies) {
            reply.removeParentComment();
        }
        commentRepository.deleteById(commentId);
    }

    public void modifyComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("에러 내용: 댓글 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 댓글입니다");
        });
        comment.updateContent(content);
    }
}
