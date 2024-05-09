package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.DATA_ALREADY_EXISTED;
import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public Long addPostLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 등록 실패 " +
                    "발생 원인: 존재하지 않는 Post의 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 등록 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });

        postLikeRepository.findByPostIdAndUserId(postId, userId).ifPresent(f -> {
            log.error("에러 내용: 좋아요 등록 실패 " +
                    "발생 원인: 한 유저는 한 게시물에 좋아요를 한개만 등록 가능함");
            throw new AppException(DATA_ALREADY_EXISTED, "게시물에 이미 좋아요를 눌렀습니다");
        });

        PostLike postLike = PostLike.createPostLike(post, user);
        postLikeRepository.save(postLike);
        return postLike.getId();
    }

    @Transactional(readOnly = true)
    public PostLike findPostLikeById(Long postLikeId) {
        return postLikeRepository.findById(postLikeId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 좋아요입니다");
        });
    }

    @Transactional(readOnly = true)
    public List<PostLike> findPostLikeByPostId(Long postId) {
        postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 Post의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });
        return postLikeRepository.findByPostId(postId);
    }

    @Transactional(readOnly = true)
    public PostLike findByPostIdAndUserId(Long postId, Long userId) {
        postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 Post의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
        
        return postLikeRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: User와 Post 간의 연관관계가 존재하지 않음");
            return new AppException(NO_DATA_EXISTED, "유저가 해당 게시물에 등록한 좋아요가 존재하지 않습니다");
        });
    }

    public void removePostLike(Long postLikeId) {
        postLikeRepository.findById(postLikeId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 제거 실패 " +
                    "발생 원인: 존재하지 않는 Post의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 좋아요입니다");
        });

        postLikeRepository.deleteById(postLikeId);
    }

    public void removePostLike(Long postId, Long userId) {
        postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 Post의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });

        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() -> {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: User와 Post 간의 연관관계가 존재하지 않음");
            return new AppException(NO_DATA_EXISTED, "유저가 해당 게시물에 등록한 좋아요가 존재하지 않습니다");
        });

        postLikeRepository.deleteById(postLike.getId());
    }
}
