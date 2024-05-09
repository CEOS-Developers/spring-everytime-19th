package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadPostLikeResponse {
    private Long id;
    private Long userId;

    public static ReadPostLikeResponse from(PostLike postLike) {
        return new ReadPostLikeResponse(postLike.getId(), postLike.getUser().getId());
    }
}
