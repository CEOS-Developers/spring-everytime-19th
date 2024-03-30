package com.ceos19.springeverytime.postlike.service;

import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.repository.PostRepository;
import com.ceos19.springeverytime.post.service.PostService;
import com.ceos19.springeverytime.postlike.domain.PostLike;
import com.ceos19.springeverytime.postlike.dto.PostLikeDto;
import com.ceos19.springeverytime.postlike.repository.PostLikeRepository;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createLike(PostLikeDto postLikeDto){
        User user = userRepository.findUserById(postLikeDto.getUserId())
                .orElseThrow(EntityNotFoundException::new);
        Post post = postRepository.findPostById(postLikeDto.getPostId())
                .orElseThrow(EntityNotFoundException::new);

        postService.increaseLike(post.getId());
        PostLike save = postLikeRepository.save(postLikeDto.toEntity(user, post));

        return save.getId();
    }

    public PostLikeDto getLikeByPostAndUser(Long postId, Long userId) throws NotFoundException {
        PostLike postLike = postLikeRepository.findByUser_IdAndPost_Id(userId, postId)
                .orElseThrow(NotFoundException::new);

        return PostLikeDto.of(postLike);
    }

    public List<PostLike> getAllLikeByPost(Long postId){
        return postLikeRepository.findByPostId(postId);
    }

    @Transactional
    public void deleteLike(PostLikeDto postLikeDto){
        postService.decreaseLike(postLikeDto.getPostId());
        postLikeRepository.deleteById(postLikeDto.getId());
    }
}
