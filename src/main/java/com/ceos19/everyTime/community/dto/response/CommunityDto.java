package com.ceos19.everyTime.community.dto.response;

import com.ceos19.everyTime.community.domain.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommunityDto {
    private String name;
    private Long id;

    public CommunityDto(Community c){
        this.name = c.getName();
        this.id= c.getId();
    }

}
