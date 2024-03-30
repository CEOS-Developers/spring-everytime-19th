package com.ceos19.everyTime.post.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListWithSliceResponseDto {
    private List<PostShortResponseDto> postShortResponseDtoList = new ArrayList<>();
    private boolean hasNextPage;

    public static PostListWithSliceResponseDto of(List<PostShortResponseDto> postShortResponseDtoList, boolean hasNextPage){
        return new PostListWithSliceResponseDto(postShortResponseDtoList,hasNextPage);
    }

}
