package com.ceos19.everytime.dto;


import com.ceos19.everytime.domain.AboutPost.Post;

public record PostResponseDTO (String title, String contents){

    public PostResponseDTO(Post post) {
        this(post.getTitle(), post.getContents());
    }

}
