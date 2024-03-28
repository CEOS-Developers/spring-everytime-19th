package com.ceos19.everyTime.post.service;

import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.domain.LikePost;
import com.ceos19.everyTime.post.domain.LikeReply;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.repository.LikePostRepository;
import com.ceos19.everyTime.post.repository.LikeReplyRepository;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeReplyService {
    private final ReplyRepository replyRepository;
    private final LikeReplyRepository likeReplyRepository;


    //Reply 에 좋아요 누를 떄 사용 메서드
    @Transactional
    public void likeReply(Member currentMember,Long replyId){
        Reply reply = findReply(replyId);

        //reply 를 작성한 작성자가 현재 좋아요를 누르려고 시도하는 사람의 ID 값이 같을 경우 예외
        if(reply.getMember().getId() == currentMember.getId()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        //이전에 좋아요 눌렀다면 삭제, 그렇지 않다면 저장.
        likeReplyRepository.findByMemberIdAndReplyId(currentMember.getId(), reply.getId())
            .ifPresentOrElse(likeReply -> {
                deleteLikeReply(likeReply,reply);
            },()->{
                saveLikeReply(currentMember,reply);
            });


    }

    private Reply findReply(Long replyId){
        return replyRepository.findById(replyId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    //답글에 좋아요 누를 때 사용 메서드
    private void saveLikeReply(Member member,Reply reply){
        LikeReply lr= LikeReply.builder()
            .reply(reply)
            .member(member)
            .build();
        reply.increaseLikeCount();

        likeReplyRepository.save(lr);
    }

    //답글에 좋아요 한 번 더 누를 시 사용 메서드
    private void deleteLikeReply(LikeReply lr,Reply reply){
        likeReplyRepository.delete(lr);
        reply.decreaseLikeCount();
    }


}
