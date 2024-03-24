package com.ceos19.everytime.dto.response;

import java.time.LocalDateTime;

public record MessageResponseDto(String senderNickname, String content, LocalDateTime transferTime) {
}
