package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadChatResponse {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private UserDto author;

    public static ReadChatResponse from(Chat chat) {
        return new ReadChatResponse(
                chat.getId(),
                chat.getContent(),
                chat.getSentAt(),
                new UserDto(chat.getAuthor().getId(),
                        chat.getAuthor().getName())
        );
    }

    @Getter
    @AllArgsConstructor
    private static class UserDto{
        private Long id;
        private String name;
    }
}
