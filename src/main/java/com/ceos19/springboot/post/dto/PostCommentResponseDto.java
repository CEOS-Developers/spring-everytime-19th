package com.ceos19.springboot.post.dto;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.users.domain.Users;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostCommentResponseDto {
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private String imageUrl;

    private Long commentId;
    private String commentContent;


    private PostCommentResponseDto(Long userId, String title, String content, LocalDateTime createdAt, int likes, String imageUrl,Long commentId,String commentContent) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.likes = likes;
        this.imageUrl = imageUrl;
        this.commentId = commentId;
        this.commentContent = commentContent;
    }

    // 정적 팩토리 메서드
    public static PostCommentResponseDto createFromPost(Post post,Comment comment) {
        return new PostCommentResponseDto(
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikes(),
                post.getImageUrl(),
                comment.getCommentId(),
                comment.getContent());
    }

    public static PostCommentResponseDto createIfNoCommentFromPost(Post post) {
        return new PostCommentResponseDto(
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikes(),
                post.getImageUrl(),
                null,
                null);
    }
}
