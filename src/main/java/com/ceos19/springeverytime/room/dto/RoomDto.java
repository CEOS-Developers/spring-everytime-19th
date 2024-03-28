package com.ceos19.springeverytime.room.dto;

import com.ceos19.springeverytime.room.domain.Room;
import com.ceos19.springeverytime.user.domain.User;
import lombok.Data;

@Data
public class RoomDto {
    private Long id;
    private Long participant1Id;
    private Long participant2Id;

    public Room toEntity(User participant1, User participant2) {
        return Room.builder()
                .participant1(participant1)
                .participant2(participant2)
                .build();
    }
}
