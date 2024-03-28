package com.ceos19.everyTime.post.service;

import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.domain.LikePost;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.repository.LikePostRepository;
import com.ceos19.everyTime.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikePostService {
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;


    //Post 에 대해 좋아요 를 누를 때를 위한 메서드
    @Transactional
    public void likePost(Member currentMember,Long postId){
        Post post = findPost(postId);

        //post 의 작성자와 좋아요를 누르려는 사람의 ID 값이 같을 떄 예외
        if(post.getMember().getId() == currentMember.getId()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }


        //이미 눌러져 있을 때 deleteLikePost 수행, 없다면 saveLikePost 수행
        likePostRepository.findByMemberIdAndPostId(currentMember.getId(), post.getId())
            .ifPresentOrElse(likePost -> {
                  deleteLikePost(likePost,post);
            },()->{
                saveLikePost(currentMember,post);
            });


    }

    private Post findPost(Long postId){
        return postRepository.findById(postId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    // 좋아요 누를 시 사용 메서드
    private void saveLikePost(Member member,Post post){
        LikePost lp= LikePost.builder()
            .post(post)
            .member(member)
            .build();
        post.increaseLikeCount();

        likePostRepository.save(lp);
    }

    //좋아요 한번 더 눌러 취소 시킬 때 사용 메서드
    private void deleteLikePost(LikePost lp,Post post){
       likePostRepository.delete(lp);
       post.decreaseLikeCount();
    }



}
