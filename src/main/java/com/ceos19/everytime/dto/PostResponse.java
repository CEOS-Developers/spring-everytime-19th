package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private Member author;
    private Board board;
    private boolean isAnonymous;
    private Long likes;

    public static PostResponse from(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getBoard(),
                post.isAnonymous(),
                post.getLikes()
        );
    }

}
