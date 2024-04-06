package com.ceos19.springboot.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private Long boardId;
    private String title;
    private String content;
    private Boolean anonymous;
}
