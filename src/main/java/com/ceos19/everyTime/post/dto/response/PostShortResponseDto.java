package com.ceos19.everyTime.post.dto.response;

import com.ceos19.everyTime.post.domain.Post;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostShortResponseDto {

    private Long postId;
    private String title;
    private String contents;
    private int replyCount;
    private int likeCount;
    private String writer;
    private LocalDateTime createdAt;


    public static PostShortResponseDto of(Post post,String writer){
        return new PostShortResponseDto(post.getId(),post.getTitle(),post.getContents(),post.getReplyCount(),post.getLikeCount(),
            writer,post.getCreatedAt());
    }

}
