package com.ceos19.everytime.domain;

import java.util.List;

import com.ceos19.everytime.dto.response.PostResponseDto;

public class Posts {

    private final List<Post> posts;

    public Posts(final List<Post> posts) {
        this.posts = posts;
    }

    public List<PostResponseDto> toResponseDto() {
        return posts.stream()
                .map(Post::toResponseDto)
                .toList();
    }
}
