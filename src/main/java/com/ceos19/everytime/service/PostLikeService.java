package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.dto.like.LikeRequest;
import com.ceos19.everytime.dto.like.LikeResponse;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public LikeResponse likePost (Long postId, LikeRequest likeRequest){

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        final Member member = memberRepository.findById(likeRequest.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if(postLikeRepository.existsByPostIdAndMemberId(post.getId(),member.getId())){
            throw new CustomException(DATA_ALREADY_EXISTED);
        }

        post.addLike();
        postLikeRepository.save(new PostLike(post,member));
        return LikeResponse.of(post.getLikes(), true);

    }

    @Transactional
    public LikeResponse cancelPostLike(Long postId, LikeRequest likeRequest){
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        final PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, likeRequest.getMemberId())
                .orElseThrow(() -> new CustomException(POST_LIKE_NOT_FOUND));

        post.cancelLike();
        postLikeRepository.delete(postLike);

        return LikeResponse.of(post.getLikes(), false);
    }

}
