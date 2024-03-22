package com.ceos19.springeverytime.postlike.service;

import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.service.PostService;
import com.ceos19.springeverytime.postlike.domain.PostLike;
import com.ceos19.springeverytime.postlike.dto.PostLikeDto;
import com.ceos19.springeverytime.postlike.repository.PostLikeRepository;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {
    private PostLikeRepository postLikeRepository;
    private PostService postService;
    private UserService userService;

    public void createLike(PostLikeDto postLikeDto){
        User user = userService.getUser(postLikeDto.getUserId());
        Post post = postService.getPost(postLikeDto.getPostId());

        postService.increaseLike(post.getId());
        postLikeRepository.save(postLikeDto.toEntity(user,post));
    }

    public PostLike getLike(Long postLikeId){
        return postLikeRepository.findById(postLikeId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteLike(Long postLikeId){
        postService.decreaseLike(postLikeId);
        postLikeRepository.deleteById(postLikeId);
    }
}
