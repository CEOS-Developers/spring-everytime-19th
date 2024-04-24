package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadCommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public static ReadCommentResponse from(Comment comment) {
        return new ReadCommentResponse(comment.getId(),
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getModifiedDate());
    }
}
