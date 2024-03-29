package com.ceos19.everytime.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class PostUpdateRequest {

    private String title;
    private String content;
    private boolean isAnonymous;
}
