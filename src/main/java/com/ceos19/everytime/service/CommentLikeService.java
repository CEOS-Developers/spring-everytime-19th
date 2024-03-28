package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.repository.CommentLikeRepository;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public void addCommentLike(Long commentId, Long memberId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Member> member = memberRepository.findById(memberId);

        if(comment.isEmpty() || member.isEmpty()){
            log.info("[Service][addCommentLike] FAIL");
        }
        else{
            CommentLike commentLike = new CommentLike(comment.get(),member.get());
            comment.get().addLike();
            log.info("[Service][addCommentLike] SUCCESS");
            commentLikeRepository.save(commentLike);
        }
    }

    public void deleteCommentLike(Long commentId, Long memberId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Member> member = memberRepository.findById(memberId);

        if(comment.isEmpty() || member.isEmpty()){
            log.info("[Service][deleteCommentLike] FAIL");
        }
        else{
            Optional<CommentLike> commentLike = commentLikeRepository.findByCommentIdAndUser(commentRepository.findById(commentId).get(),memberRepository.findById(memberId).get());
            if(commentLike.isPresent()){
                comment.get().deleteLike();
                commentLikeRepository.delete(commentLike.get());
                log.info("[Service][deleteCommentLike] SUCCESS");
            }
            else{
                log.info("[Service][deleteCommentLike] FAIL");
            }
        }
    }

}
