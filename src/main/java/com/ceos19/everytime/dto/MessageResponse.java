package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.ReadStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class MessageResponse {

    private Long id;
    private Member sender;
    private Member receiver;
    private String content;
    private ReadStatus readStatus;

}
