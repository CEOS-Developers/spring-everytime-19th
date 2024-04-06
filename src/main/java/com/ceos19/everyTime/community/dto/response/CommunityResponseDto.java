package com.ceos19.everyTime.community.dto.response;

import com.ceos19.everyTime.community.domain.Community;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityResponseDto {
    private String name;
    private Long id;

    public static CommunityResponseDto from(Community c){
        return new CommunityResponseDto(c.getName(),c.getId());
    }

}
