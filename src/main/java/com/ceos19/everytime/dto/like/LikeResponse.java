package com.ceos19.everytime.dto.like;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class LikeResponse {

    private Long likes;
    private Boolean liked;

    public static LikeResponse of(Long likes, Boolean liked){
        return new LikeResponse(
                likes,
                liked
        );
    }
}
