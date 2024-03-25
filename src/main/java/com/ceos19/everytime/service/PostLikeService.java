package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.PostLikeRepository;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void addPostLike(Long postId, Long memberId){
        Optional<Post> post = postRepository.findById(postId);
        Optional<Member> member = memberRepository.findById(memberId);

        if(post.isEmpty() || member.isEmpty()){
            log.info("[Service][addPostLike] FAIL");
        }
        else{
            PostLike postLike = new PostLike(post.get(), member.get());
            post.get().addLike();
            log.info("[Service][addPostLike] SUCCESS");
            postLikeRepository.save(postLike);
        }
    }

    public void deletePostLike(Long postId, Long memberId){
        Optional<Post> post = postRepository.findById(postId);
        Optional<Member> member = memberRepository.findById(memberId);

        if(post.isEmpty() || member.isEmpty()){
            log.info("[Service][cancelPostLike] FAIL");
        }
        else{
            Optional<PostLike> postLike = postLikeRepository.findByPostIdAndUser(postRepository.findById(postId).get(),memberRepository.findById(memberId).get());
            if(postLike.isPresent()){
                post.get().deleteLike();
                postLikeRepository.delete(postLike.get());
                log.info("[Service][cancelPostLike] SUCCESS");
            }
            else{
                log.info("[Service][cancelPostLike] FAIL");
            }
        }
    }


}
