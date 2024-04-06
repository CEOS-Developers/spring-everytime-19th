package com.ceos19.everyTime.jjokji.dto.response;

import com.ceos19.everyTime.jjokji.domain.Jjokji;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JjokjiResponseDto {
    private String message;
    private LocalDateTime createdAt;
    private Long senderId;



    public static JjokjiResponseDto from(Jjokji jjokji){
        return new JjokjiResponseDto(jjokji.getMessage(),jjokji.getCreatedAt(),jjokji.getMember().getId());
    }
}
