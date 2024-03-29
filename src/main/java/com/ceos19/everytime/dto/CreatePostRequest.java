package com.ceos19.everytime.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CreatePostRequest {

    private String title;
    private String content;
    private Long authorId;
    private Long boardId;
    private boolean isAnonymous;

}
