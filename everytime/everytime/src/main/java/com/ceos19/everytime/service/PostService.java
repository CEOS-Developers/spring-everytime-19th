package com.ceos19.everytime.service;


import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.dto.CommentDTO;
import com.ceos19.everytime.dto.PostDTO;
import com.ceos19.everytime.repository.CommentRepository;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    //1. 게시글 id로 조회
    @Transactional(readOnly = true)
    public Post getPostById(long postId) {
        return postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Not Found: "+postId));
    }

    //2. 게시글 작성하기
    @Transactional
    public Post savePost(PostDTO postDTO){
        return postRepository.save(postDTO.toPost(0L));
    }

    //3. 게시글에 댓글 및 대댓글 기능
    @Transactional
    public Comment addCommentToPost(CommentDTO commentdto) {

        Comment addedComment = commentdto.toComment(); //DTO에서 Domain으로 전환
        Post post = getPostById(addedComment.getPost().getPostId());//게시글 ID로 게시글 찾기

        return commentRepository.save(addedComment);// 댓글 저장
    }


    //4. 게시글에 좋아요 기능
    @Transactional
    public Post addLikeToPost(long postId){
        //postId로 게시글 조회 -> 존재하는지 Optional을 통해 확인
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            post.addLikeNum(post.getLikeNum()); // 좋아요 수 +1
            post = postRepository.save(post); // 변경 사항 저장
            return post;
        } else {
            // 게시글을 찾을 수 없는 경우, 적절한 예외 처리 또는 로직을 구현
            throw new RuntimeException("Not Found: " + postId);
        }
    }

    //5. 게시글 삭제 기능
    @Transactional
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

    //6. 게시글 좋아요 삭제 기능
    @Transactional
    public Post deleteLikeNum(long postId) {
        //postId로 게시글 조회 -> 존재하는지 Optional을 통해 확인
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            post.deleteLikeNum(post.getLikeNum()); // 좋아요 수 -1
            post = postRepository.save(post); // 변경 사항 저장
            return post;
        } else {
            // 게시글을 찾을 수 없는 경우, 적절한 예외 처리 또는 로직을 구현
            throw new RuntimeException("Not Found: " + postId);
        }
    }

    //7. 게시글 댓글 삭제 기능
    @Transactional
    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    //8. 게시글 대댓글 삭제 기능
    @Transactional
    public Comment deleteSubcomment(long commentId) {
        //postId로 게시글 조회 -> 존재하는지 Optional을 통해 확인
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            comment.deleteSubcomment(comment.getLikeNum()); // 좋아요 수 -1
            comment = commentRepository.save(comment); // 변경 사항 저장
            return comment;
        } else {
            // 게시글을 찾을 수 없는 경우, 적절한 예외 처리 또는 로직을 구현
            throw new RuntimeException("Not Found: " + commentId);
        }
    }
}
