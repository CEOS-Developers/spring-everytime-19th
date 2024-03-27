package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public Long addPost(Post post, Attachment... attachments) {
        postRepository.save(post);
        for (Attachment attachment : attachments) {
            if (attachment.getId() != null) {
                log.error("에러 내용: 게시물 등록 실패 " +
                    "발생 원인: 이미 다른 곳에 등록된 파일을 해당 게시물에 재 등록함");
                throw new AppException(DATA_ALREADY_EXISTED, "이미 등록된 파일입니다");
            }
            post.addAttachment(attachment);
        }
        return post.getId();
    }

    public Post findPostById(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        }
        return optionalPost.get();
    }

    public List<Post> findPostByAuthorId(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public void removePost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        }

        // 연관관계 제거
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            comment.removeParentComment();
        }

        // 연관관계 제거
        commentRepository.deleteAllByPostId(postId);

        // 연관관계 제거
        postLikeRepository.deleteAllByPostId(postId);

        postRepository.deleteById(postId);
    }
}
