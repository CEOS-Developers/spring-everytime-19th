package com.ceos19.springeverytime.postlike.dto;

import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.postlike.domain.PostLike;
import com.ceos19.springeverytime.user.domain.User;
import lombok.Data;

@Data
public class PostLikeDto {
    private Long id;
    private Long postId;
    private Long userId;

    public PostLike toEntity(User user, Post post){
        return PostLike.builder()
                .user(user)
                .post(post)
                .build();
    }
}
