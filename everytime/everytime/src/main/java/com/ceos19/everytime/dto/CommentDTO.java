package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutUser.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentDTO {

    private String contents;

    private Long likeNum;

    private boolean isDeleted;

    private boolean isReported;

    private Comment parentComment;

    private User user;

    public Comment toComment() {  //생성자를 통해 객체 생성
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
