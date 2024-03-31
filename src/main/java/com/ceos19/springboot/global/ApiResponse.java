package com.ceos19.springboot.global;

import lombok.Getter;
import lombok.Builder;

@Getter
public class ApiResponse<T> {

    // API 응답 결과 Response
    private T result;

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
    public static <T> ApiResponse<T> of(int resultCode, String resultMsg, T result) {
        return new ApiResponse<>(result,resultCode,resultMsg);
    }
}

