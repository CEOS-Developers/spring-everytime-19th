package com.ceos19.everytime.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddChattingRoomRequest {
    private Long participant1_id;
    private Long participant2_id;
}
