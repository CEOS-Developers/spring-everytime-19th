package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.PostLike;
import com.ceos19.springboot.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void unLikePost(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }
}
