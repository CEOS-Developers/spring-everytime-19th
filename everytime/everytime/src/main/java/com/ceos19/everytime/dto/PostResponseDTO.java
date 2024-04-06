package com.ceos19.everytime.dto;


import com.ceos19.everytime.domain.AboutPost.Post;
import lombok.Builder;

@Builder
public record PostResponseDTO (String title, String contents){


    public static PostResponseDTO from(Post post){
        return PostResponseDTO.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .build();
    }

}
