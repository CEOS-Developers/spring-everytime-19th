package com.ceos19.springboot.postlike.service;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.postlike.domain.PostLike;
import com.ceos19.springboot.postlike.repository.PostLikeRepository;
import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void unLikePost(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }

    @Transactional
    public void pressLikePost(Users user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글이 없습니다"));
        PostLike postLike = PostLike.builder()
                        .user(user).post(post).build();
        PostLike savedData = postLikeRepository.save(postLike);
    }

    public boolean alreadyLiked(Long userId, Long postId) {
        Optional<PostLike> findPostLike = postLikeRepository.findByUserIdAndPostId(userId, postId);
        if (findPostLike.isPresent())
            return true;
        else
            return false;
    }
}
