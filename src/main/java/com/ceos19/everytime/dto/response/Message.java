package com.ceos19.everytime.dto.response;

import java.time.LocalDateTime;

public record Message(String content, LocalDateTime transferTime) {
}
