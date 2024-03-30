package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutUser.User;


public record CommentDTO (String contents, Long likeNum, boolean isDeleted, boolean isReported, Comment parentComment, User user) {
    public Comment toComment() {
        return Comment.builder()
                .contents(contents)
                .likeNum(likeNum)
                .isDeleted(isDeleted)
                .isReported(isReported)
                .parentComment(parentComment)
                .user(user)
                .build();
    }
}
