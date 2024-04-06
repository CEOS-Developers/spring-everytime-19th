package com.ceos19.springboot.post.dto;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.users.domain.Users;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private String imageUrl;

    private PostResponseDto(Long postId, Long userId, String title, String content, LocalDateTime createdAt, int likes, String imageUrl) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.likes = likes;
        this.imageUrl = imageUrl;
    }

    // 정적 팩토리 메서드
    public static PostResponseDto createFromPost(Post post, Long postId) {
        return new PostResponseDto(
                postId,
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikes(),
                post.getImageUrl());
    }

    public static PostResponseDto updateFromPost(Post post, Long postId) {
        return new PostResponseDto(
                postId,
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikes(),
                post.getImageUrl());
    }
}
