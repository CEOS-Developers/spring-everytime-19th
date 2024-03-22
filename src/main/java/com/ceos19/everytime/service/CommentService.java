package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.MemberRepository;
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
public class CommentService {
    public static final int MAX_CONTENT_LENGTH = 1000;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Comment create(Long postId, Long parentCommentId, Long memberId, String content, boolean isAnonymous){
        Optional<Post> post = postRepository.findById(postId);
        Optional<Member> member = memberRepository.findById(memberId);

        if(post.isEmpty() || member.isEmpty() || !validateContent(content)){
            log.info("[Service][create] FAIL");
            return null;
        }

        Comment comment;

        if(parentCommentId == null){
            comment = new Comment(post.get(),null,member.get(),content,isAnonymous);
            log.info("[Service][create] SUCCESS - comment");
        }
        else{   //대댓글인지 확인
            Optional<Comment> parentComment = commentRepository.findById(parentCommentId);
            if(parentComment.isEmpty()){
                log.info("[Service][create] FAIL");
                return null;
            }
            comment = new Comment(post.get(),parentComment.get(),member.get(),content,isAnonymous);
            log.info("[Service][create] SUCCESS - reply");
        }

        commentRepository.save(comment);
        return comment;
    }

    public void delete(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()){
            log.info("[Service][delete] FAIL");
        }
        else{
            commentRepository.delete(comment.get());
            log.info("[Service][delete] SUCCESS");
        }
    }

    public Comment updateContent(Long commentId, String content){
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty() || !validateContent(content)){
            log.info("[Service][updateContent] FAIL");
            return null;
        }

        comment.get().changeContent(content);
        comment.get().updateDeleteStatus();
        log.info("[Service][updateContent] SUCCESS");
        return comment.get();
    }

    private boolean validateContent(String content){
        return !content.isEmpty() && content.length() <= MAX_CONTENT_LENGTH;
    }

}
