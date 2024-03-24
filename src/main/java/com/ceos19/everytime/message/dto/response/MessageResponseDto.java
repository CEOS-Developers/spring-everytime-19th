package com.ceos19.everytime.message.dto.response;

import java.time.LocalDateTime;

public record MessageResponseDto(String senderNickname, String content, LocalDateTime transferTime) {
}
