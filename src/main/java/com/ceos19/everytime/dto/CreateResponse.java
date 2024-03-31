package com.ceos19.everytime.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CreateResponse {

    private Long id;

    public static CreateResponse from (Long id){
        return new CreateResponse(id);
    }
}
