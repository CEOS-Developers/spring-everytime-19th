package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.University;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class BoardResponse {
    private Long id;
    private String boardName;
    private String description;
    private Member boardManager;
    private University university;
    private List<Post> posts = new ArrayList<>();

}
