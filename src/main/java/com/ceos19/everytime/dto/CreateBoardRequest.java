package com.ceos19.everytime.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CreateBoardRequest {

    private String boardName;
    private String description;
    private Long boardManagerId;
    private Long universityId;

}
