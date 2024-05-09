package com.ceos19.springboot.post.service;

import com.ceos19.springboot.common.code.ErrorCode;
import com.ceos19.springboot.global.exception.BusinessExceptionHandler;
import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.post.dto.CombinedDto;
import com.ceos19.springboot.post.dto.PostModifyRequestDto;
import com.ceos19.springboot.post.dto.PostRequestDto;
import com.ceos19.springboot.postlike.repository.PostLikeRepository;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    // 게시글 저장
    @Transactional
    public Post createPost(PostRequestDto post,Users user) {
        Post createdPost = Post.builder()
                .createdAt(LocalDateTime.now())
                .content(post.getContent())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .user(user)
                .likes(0)
                .build();
        return postRepository.save(createdPost);
    }

    // 게시글 전체 조회
    public List<Post> retrievePost() {
        return postRepository.findAll();
    }

    //게시글을 페이징 하여 조회
    public Page<Post> retrievePostsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // page는 조회하고자 하는 페이지 번호, size는 페이지 당 게시글 수
        return postRepository.findAll(pageable);
    }

    public Post retreiveOnePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
    }

    //게시글에 좋아요 누르기
    @Transactional
    public Post pressLike(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        findPost.plusLike();
        return findPost;
    }

    @Transactional
    public void unLike(Post post) {
        Post findPost = postRepository.findById(post.getPostId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        findPost.minusLike();
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        commentRepository.deleteByPost(post);
        postLikeRepository.deleteByPost(post);

        postRepository.delete(post);
    }

    public boolean isOwner(Long postId, Users user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        if(user.equals(post.getUser())){ // 글의 주인이라면
            return true;
        } else {
            return false;
            //throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }
    }

    @Transactional
    public Post updatePost(Long postId, PostModifyRequestDto postModifyRequestDto) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 Post를 찾을 수 없습니다"));
        findPost.updateFromDto(postModifyRequestDto);
        return findPost;
    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
    }
}
