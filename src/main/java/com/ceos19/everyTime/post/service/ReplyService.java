package com.ceos19.everyTime.post.service;

import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.dto.request.ReplyCommentSaveRequestDto;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ReplyService {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final PostService postService;

    private static final String REPLY_DEFAULT_NICK_NAME_FOR_POST_OWNER="작성자(익명)";
    private static final String DELETED_CONTENTS="(삭제된 댓글)";





    //답글 저장.
    @Transactional
    public void saveComment(Long postId, ReplyCommentSaveRequestDto replyCommentSaveRequestDto,
        Member currentMember){
        Post post = findPost(postId);

        //댓글일 경우
        Reply parentReply=null;

        //대댓글일 경우
        if(replyCommentSaveRequestDto.getParentId()!=null){
            parentReply=replyRepository.findById(replyCommentSaveRequestDto.getParentId()).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
        }

        Reply reply = Reply.builder()
            .member(currentMember)
            .parent(parentReply)
            .contents(replyCommentSaveRequestDto.getComment())
            .post(post)
            .isHideNickName(replyCommentSaveRequestDto.isHideNickName())
            .writer(makeWriter(currentMember,post, replyCommentSaveRequestDto.isHideNickName()))
            .build();


        post.increaseReplyCount();

        replyRepository.save(reply);

    }

    // 테스트 코드를 위한 로직, 이런 식으로 하면 안될 것 같은데 쓰읍.

    public Reply saveTestComment(Long postId, ReplyCommentSaveRequestDto replyCommentSaveRequestDto,
        Member currentMember){
        Post post = findPost(postId);

        Reply parentReply=null;

        if(replyCommentSaveRequestDto.getParentId()!=null){
            parentReply=replyRepository.findById(replyCommentSaveRequestDto.getParentId()).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
        }


        Reply reply = Reply.builder()
            .member(currentMember)
            .parent(parentReply)
            .contents(replyCommentSaveRequestDto.getComment())
            .post(post)
            .isHideNickName(replyCommentSaveRequestDto.isHideNickName())
            .writer(makeWriter(currentMember,post, replyCommentSaveRequestDto.isHideNickName()))
            .build();

        post.increaseReplyCount();

        return reply;

    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long replyId,Member currentMember){
        Reply reply=findReply(replyId);

        //reply 를 작성한 사람이 현재 Relpy 를 제거하려는 사람과 다른 경우 예외
        if(reply.getMember().getId()!=currentMember.getId()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }


        Post post= findPost(reply.getPost().getId());
        post.decreaseReplyCount();

        /*// reply가 대댓글이 아닌 댓글이며, reply 에 대댓글이 달린 경우.
        if(reply.getParent()==null&&replyRepository.existsByParentId(reply.getId())){
           ChangeMessageByDelete(reply);
           return;
        }*/

        //답글 삭제.
        replyRepository.delete(reply);
    }



    private Post findPost(Long postId){
        return postRepository.findById(postId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }


    private Reply findReply(Long replyId){
        //return replyRepository.findById(replyId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
        return replyRepository.findNotDeletedReply(replyId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    private void ChangeMessageByDelete(Reply reply){

        reply.changeParentByDeleteOnlyHaveChild(DELETED_CONTENTS);

    }

    private String makeWriter(Member member,Post post,boolean isHideNickName){
        //익명 체크가 아닌 경우 : 닉네임 반환.
        if(!isHideNickName){
            return member.getNickName();
        }

        // POST를 작성한 Member가 댓글을 달 경우 작성자(익명) 으로 표시 위함.
        if(member.getId()==post.getMember().getId()){
            return REPLY_DEFAULT_NICK_NAME_FOR_POST_OWNER;
        }

        // 이전에 익명으로 댓글을 남긴 경우 그 Writer를 확인해서 반환(익명1) 그렇지 않고 새롭게 작성한 작성자의 경우 익명번호를 하나 증가시켜 반환.
        return  replyRepository.findDefaultNickNameReplyByMemberIdAndPostIdLimitOne(member.getId(),post.getId())
            .map(Reply::getWriter).orElseGet(()->postService.makeNextNickNameForHideNickName(post));
    }



}
