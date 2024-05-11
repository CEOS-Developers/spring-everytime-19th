package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutUser.User;
import lombok.Builder;

@Builder
public record CommentDTO (String contents) {
    public static CommentDTO fromComment(Comment comment) {
        return new CommentDTO(comment.getContents());
    }
    public Comment toComment() {
        return Comment.builder()
                .contents(contents)
                .build();
    }
}
