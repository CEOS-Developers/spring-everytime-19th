package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.dto.like.LikeResponse;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.security.CustomUserDetails;
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

    @Transactional
    public LikeResponse likePost (CustomUserDetails userDetails, Long postId){

        final Member member = userDetails.getMember();
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        boolean liked = false;

        if(postLikeRepository.existsByPostIdAndMemberId(post.getId(), member.getId())){  // 좋아요가 눌러져 있으면
            post.cancelLike();
        }
        else{
            post.addLike();
            liked=true;
            postLikeRepository.save(new PostLike(post,member));
        }

        return LikeResponse.of(post.getLikes(), liked);
    }

    /*

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
*/
}
