package com.ceos19.everyTime.community.dto.response;

import com.ceos19.everyTime.community.domain.Community;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommunityListResponseDto {
    List<CommunityDto> communityDtoList =new ArrayList<>();

    @Builder
    public CommunityListResponseDto(List<CommunityDto> communityDtoList){
        this.communityDtoList = communityDtoList;
    }

}
