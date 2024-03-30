package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.AboutPost.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostDTO {
    private String title;

    private String contents;
    private Long likeNum;
    public Post toPost(Long likeNum) {  //생성자를 통해 객체 생성
        return Post.builder()
                .title(title)
                .contents(contents)
                .likeNum(likeNum)
                .build();

    }
}
