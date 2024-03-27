package com.ceos19.springboot.post.dto;

import com.ceos19.springboot.comment.dto.CommentResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long postId;
    private String title;
    private Boolean anonymous;
    private Integer view;
    private Integer likes;
    private List<CommentResponseDto> commentResponseDtoList;

    public PostResponseDto(Long postId, String title, Boolean anonymous, Integer view, Integer likes, List<CommentResponseDto> commentResponseDtoList) {
        this.postId = postId;
        this.title = title;
        this.anonymous = anonymous;
        this.view = view;
        this.likes = likes;
        this.commentResponseDtoList = commentResponseDtoList;
    }


}
