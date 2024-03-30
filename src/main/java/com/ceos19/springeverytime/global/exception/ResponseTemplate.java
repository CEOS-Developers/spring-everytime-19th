package com.ceos19.springeverytime.global.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@AllArgsConstructor
public class ResponseTemplate {
    public int status;

    public String message;

    private final LocalDateTime timestamp = LocalDateTime.now();

    public static ResponseEntity<ResponseTemplate> toResponseEntity(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(ResponseTemplate.builder()
                        .message(message)
                        .status(status.value())
                        .build());
    }
}
