package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {
    public static final int MAX_CONTENT_LENGTH = 1000;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Comment create(Long postId, Long parentCommentId, Long memberId, String content, boolean isAnonymous){
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        Comment comment;

        if(parentCommentId == null){
            comment = new Comment(post,null,member,content,isAnonymous);
        }
        else{   //대댓글인지 확인
            Optional<Comment> parentComment = commentRepository.findById(parentCommentId);
            if(parentComment.isEmpty()){        // 부모 댓글 id 잘못 됨
                throw new CustomException(COMMENT_NOT_FOUND);
            }
            comment = new Comment(post,parentComment.get(),member,content,isAnonymous);
        }

        commentRepository.save(comment);
        return comment;
    }

    public void delete(Long commentId){
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }

    public void updateContent(Long commentId, String content){
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        comment.changeContent(content);
        comment.updateDeleteStatus();
    }

}
