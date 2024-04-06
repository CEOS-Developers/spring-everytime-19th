package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.CommentLikeRepository;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void likeComment (Long commentId, Long memberId){

        final Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if(commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(),member.getId())){
            throw new CustomException(DATA_ALREADY_EXISTED);
        }

        CommentLike commentLike = new CommentLike(comment, member);
        comment.addLike();
        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void cancelCommentLike(Long commentId, Long memberId){
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        final CommentLike commentLike = commentLikeRepository.findByCommentIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        comment.cancelLike();
        commentLikeRepository.delete(commentLike);
    }

}
