package com.ceos19.springeverytime.message.dto;

import com.ceos19.springeverytime.message.domain.ReadStatus;
import com.ceos19.springeverytime.room.domain.Room;
import com.ceos19.springeverytime.message.domain.Message;
import com.ceos19.springeverytime.user.domain.User;
import lombok.Data;

@Data
public class MessageDto {
    private Long id;
    private Long senderId;
    private Long roomId;
    private String content;

    public Message toEntity(Room room, User user) {
        return Message.builder()
                .room(room)
                .sender(user)
                .isRead(ReadStatus.NOT_READ)
                .content(content)
                .build();
    }
}
