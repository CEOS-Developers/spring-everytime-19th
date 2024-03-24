package com.ceos19.everyTime.post.dto.response;

import com.ceos19.everyTime.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostShortResponseDto {

    private Long postId;
    private String title;
    private String contents;
    private int replyCount;
    private int likeCount;
    private String writer;
    private LocalDateTime createdAt;


    public PostShortResponseDto(Post post,String writer){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.replyCount = post.getReplyCount();
        this.likeCount = post.getLikeCount();
        this.writer = writer;
        this.createdAt = post.getCreatedAt();
    }

}
