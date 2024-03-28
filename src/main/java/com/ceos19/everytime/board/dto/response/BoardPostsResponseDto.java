package com.ceos19.everytime.board.dto.response;

import com.ceos19.everytime.post.domain.Post;

public record BoardPostsResponseDto(String title, String writerNickname) {
    public static BoardPostsResponseDto from(final Post post) {
        return new BoardPostsResponseDto(post.getTitle(), post.getWriterNickname());
    }
}
