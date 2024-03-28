package com.ceos19.springeverytime.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    NOT_FOUND_USER_ID(1001, "요청한 ID에 해당하는 멤버가 존재하지 않습니다."),
    NOT_FOUND_CATEGORY_ID(1002, "요청한 ID에 해당하는 카테고리가 존재하지 않습니다."),
    NOT_FOUND_POST_ID(1003, "요청한 ID에 해당하는 게시글이 존재하지 않습니다."),
    NOT_FOUND_IMAGE_ID(1004, "요청한 ID에 해당하는 이미지가 존재하지 않습니다."),
    NOT_FOUND_POST_WITH_MEMBER(1005, "요청한 멤버 ID와 포스트 ID에 해당하는 게시글이 존재하지 않습니다."),

    SHOULD_EXCEED_14_DAYS_TO_DELETE(2001, "카테고리를 삭제하려면 생성 후 14일이 지나야 합니다.");

    private final int code;
    private final String message;
}
