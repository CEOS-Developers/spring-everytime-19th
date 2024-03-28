package com.ceos19.everyTime.jjokji.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LatestJjokjiListResponse {
    List<JjokjiResponse> latestJjokjiResponseList = new ArrayList<>();

    @Builder
    public LatestJjokjiListResponse(List<JjokjiResponse> latestJjokjiResponseList){
        this.latestJjokjiResponseList = latestJjokjiResponseList;
    }

}
