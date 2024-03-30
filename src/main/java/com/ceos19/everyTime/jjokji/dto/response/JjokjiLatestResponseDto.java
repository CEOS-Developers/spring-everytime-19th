package com.ceos19.everyTime.jjokji.dto.response;

import com.ceos19.everyTime.jjokji.domain.Jjokji;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class JjokjiLatestResponseDto {

    private String message;
    private LocalDateTime createdAt;
    private Long chatRoomId;


    public static JjokjiLatestResponseDto from(Jjokji jjokji){
        return new JjokjiLatestResponseDto(jjokji.getMessage(), jjokji.getCreatedAt(),jjokji.getJjokjiRoom().getId());
    }


}
