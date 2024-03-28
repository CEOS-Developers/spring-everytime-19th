package com.ceos19.springboot.common.api;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse {
    private int status;
    private String message;

    @Builder
    private SuccessResponse(int status, String message)
    {
        this.message = message;
        this.status = status;
    }
    public static SuccessResponse of(HttpStatus status, String message)
    {
        return SuccessResponse.builder()
                .status(status.value())
                .message(message)
                .build();
    }

}
