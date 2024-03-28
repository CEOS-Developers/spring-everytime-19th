package com.ceos19.everytime.message.dto.request;

public record MessageRequestDto(Long senderId, Long receiverId, String content) {
}
