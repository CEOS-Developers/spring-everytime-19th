package com.ceos19.springboot.common.api;

import lombok.Getter;

@Getter
public class ResponseUtils {

    // 성공
    public static <T> ApiResponseDto<T> ok (T response)
    {
        return ApiResponseDto.<T>builder()
                .success(true)
                .response(response)
                .build();
    }
    // 에러발생
    public static <T> ApiResponseDto<T> error(ErrorResponse response)
    {
        return ApiResponseDto.<T>builder()
                .success(false)
                .error(response)
                .build();
    }
}
