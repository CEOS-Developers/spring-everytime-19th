package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CreateMessageRequest {

    @NotNull(message = "발신자 아이디를 적어주세요.")
    private Long senderId;

    @NotNull(message = "수신자 아이디를 적어주세요.")
    private Long receiverId;

    @NotBlank(message = "변경할 쪽지 내용을 작성해주세요.")
    private String content;

}

