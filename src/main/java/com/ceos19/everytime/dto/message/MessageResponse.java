package com.ceos19.everytime.dto.message;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Message;
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

    public static MessageResponse from(Message message){
        return new MessageResponse(
                message.getId(),
                message.getSender(),
                message.getReceiver(),
                message.getContent(),
                message.getReadStatus()
        );
    }

}
