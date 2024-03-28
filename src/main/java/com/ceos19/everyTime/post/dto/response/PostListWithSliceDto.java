package com.ceos19.everyTime.post.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostListWithSliceDto {
    private List<PostShortResponseDto> postShortResponseDtoList = new ArrayList<>();
    private boolean hasNextPage;

}
