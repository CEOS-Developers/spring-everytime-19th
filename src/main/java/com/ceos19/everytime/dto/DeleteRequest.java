package com.ceos19.everytime.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DeleteRequest {
    private Long memberId;
}
