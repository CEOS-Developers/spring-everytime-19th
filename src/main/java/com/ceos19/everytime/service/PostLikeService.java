package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.dto.LikeRequest;
import com.ceos19.everytime.dto.LikeResponse;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos19.everytime.exception.ErrorCode.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public LikeResponse likePost (Long postId, LikeRequest likeRequest){

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        final Member member = memberRepository.findById(likeRequest.getMemberId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        if(postLikeRepository.existsByPostIdAndMemberId(post.getId(),member.getId())){
            throw new CustomException(DATA_NOT_FOUND);
        }

        post.addLike();
        postLikeRepository.save(new PostLike(post,member));
        return new LikeResponse(post.getLikes(), true);

    }

    public LikeResponse cancelLikeMessage (Long postId, LikeRequest likeRequest){
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        final PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, likeRequest.getMemberId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        post.cancelLike();
        postLikeRepository.delete(postLike);

        return new LikeResponse(post.getLikes(), false);
    }

}
