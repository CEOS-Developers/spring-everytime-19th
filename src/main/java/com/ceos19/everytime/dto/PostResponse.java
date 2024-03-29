package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private Member author;
    private Board board;
    private boolean isAnonymous;
    private int likes;

}
