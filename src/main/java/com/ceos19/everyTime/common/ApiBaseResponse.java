package com.ceos19.everyTime.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiBaseResponse<T> {
    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";




    private String status;
    private String message;
    private T data;

    public static <T> ApiBaseResponse<T> createSuccess(T data) {
        return new ApiBaseResponse<>(SUCCESS_STATUS, data, null);
    }

    public static ApiBaseResponse<?> createSuccessWithNoContent() {
        return new ApiBaseResponse<>(SUCCESS_STATUS, null, null);
    }



    // 예외 발생으로 API 호출 실패시 반환
    public static ApiBaseResponse<?> createError(String message) {
        return new ApiBaseResponse<>(ERROR_STATUS, null, message);
    }
    private ApiBaseResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }



}
