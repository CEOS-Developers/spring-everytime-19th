package com.ceos19.everytime.post.dto.response;

import java.util.List;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.post.domain.Post;

public record PostResponseDto(String title, String content, String username, String boardName, List<Comment> comments) {

    public static PostResponseDto of(final Post post, List<Comment> comments) {
        return new PostResponseDto(post.getTitle(), post.getContent(), post.getWriterNickname(),
                post.getBoard().getName(), comments);
    }
}
