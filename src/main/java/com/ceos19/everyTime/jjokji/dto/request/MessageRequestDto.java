package com.ceos19.everyTime.jjokji.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageRequestDto {

    @NotBlank(message = "메시지를 입력해주세요")
    @Schema(description = "메시지 내용", example = "족보구해요")
    private String message;


    @NotNull(message = "senderId 를 입력해주세요")
    @Schema(description = "송신자 ID",example = "1")
    private Long senderId;

    @NotNull(message = "receiverId 를 입력해주세요")
    @Schema(description = "수신자 ID",example = "2")
    private Long receiverId;

    @Builder
    public MessageRequestDto(String message,Long senderId,Long receiverId){
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }



}
