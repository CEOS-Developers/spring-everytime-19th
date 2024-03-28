package com.ceos19.everyTime.community.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommunitySaveDto {
    private String name;

    @Builder
    public CommunitySaveDto(String name){
        this.name=name;
    }

}
