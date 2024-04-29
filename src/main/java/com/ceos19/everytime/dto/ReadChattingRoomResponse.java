package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.ChattingRoom;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadChattingRoomResponse {
    private Long id;

    private UserDto participant1;
    private UserDto participant2;


    public static ReadChattingRoomResponse from(ChattingRoom chattingRoom) {
        return new ReadChattingRoomResponse(
                chattingRoom.getId(),
                new UserDto(
                        chattingRoom.getParticipant1().getId(),
                        chattingRoom.getParticipant1().getName()
                ),
                new UserDto(
                        chattingRoom.getParticipant2().getId(),
                        chattingRoom.getParticipant2().getName()
                )
        );
    }

    @Getter
    @AllArgsConstructor
    private static class UserDto {
        private Long id;
        private String name;
    }
}
