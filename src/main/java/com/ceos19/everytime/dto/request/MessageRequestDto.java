package com.ceos19.everytime.dto.request;

public record MessageRequestDto(Long senderId, Long receiverId, String content) {
}
