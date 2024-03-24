package com.ceos19.everytime.post.domain;

import java.util.List;

import com.ceos19.everytime.board.dto.response.BoardPostsResponseDto;

public class Posts {

    private final List<Post> posts;

    public Posts(final List<Post> posts) {
        this.posts = posts;
    }

    public List<BoardPostsResponseDto> toResponseDto() {
        return posts.stream()
                .map(Post::toResponseDto)
                .toList();
    }
}
