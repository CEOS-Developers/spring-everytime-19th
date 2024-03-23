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

    public Long save(Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        }
        Post post = optionalPost.get();

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        }
        User user = optionalUser.get();

        Optional<PostLike> optionalPostLike = postLikeRepository.findByPostIdAndUserId(postId, userId);
        if (optionalPostLike.isPresent()) {
            log.error("에러 내용: 좋아요 등록 실패 " +
                    "발생 원인: 한 유저는 한 게시물에 좋아요를 한개만 등록 가능함");
            throw new AppException(DATA_ALREADY_EXISTED, "게시물에 이미 좋아요를 눌렀습니다");
        }

        PostLike postLike = PostLike.createPostLike(post, user);
        postLikeRepository.save(postLike);
        return postLike.getId();
    }

    @Transactional(readOnly = true)
    public PostLike findById(Long postLikeId) {
        Optional<PostLike> optionalPostLike = postLikeRepository.findById(postLikeId);
        if (optionalPostLike.isEmpty()) {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 좋아요입니다");
        }
        return optionalPostLike.get();
    }

    @Transactional(readOnly = true)
    public List<PostLike> findByPostId(Long postId) {
        return postLikeRepository.findByPostId(postId);
    }

    @Transactional(readOnly = true)
    public PostLike findByPostIdAndUserId(Long postId, Long userId) {
        Optional<PostLike> optionalPostLike = postLikeRepository.findByPostIdAndUserId(postId, userId);
        if (optionalPostLike.isEmpty()) {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 FK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 좋아요입니다");
        }
        return optionalPostLike.get();
    }

    public void deletePostLike(Long postLikeId) {
        Optional<PostLike> optionalPostLike = postLikeRepository.findById(postLikeId);
        if (optionalPostLike.isEmpty()) {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 좋아요입니다");
        }
        postLikeRepository.deleteById(postLikeId);
    }

    public void deletePostLike(Long postId, Long userId) {
        Optional<PostLike> optionalPostLike = postLikeRepository.findByPostIdAndUserId(postId, userId);
        if (optionalPostLike.isEmpty()) {
            log.error("에러 내용: 좋아요 조회 실패 " +
                    "발생 원인: 존재하지 않는 FK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 좋아요입니다");
        }
        PostLike postLike = optionalPostLike.get();
        postLikeRepository.deleteById(postLike.getId());
    }
}
