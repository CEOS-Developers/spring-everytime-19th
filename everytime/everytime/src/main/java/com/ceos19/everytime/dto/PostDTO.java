package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.AboutPost.Post;


public record PostDTO (String title, String contents, Long likeNum){
    public Post toPost() {
        return Post.builder()
                .title(title)
                .contents(contents)
                .likeNum(likeNum)
                .build();

    }
}
