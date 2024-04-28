package com.ceos19.springeverytime.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    NOT_FOUND_USER_ID(1001, "요청한 ID에 해당하는 유저가 존재하지 않습니다."),
    NOT_FOUND_CATEGORY_ID(1002, "요청한 ID에 해당하는 카테고리가 존재하지 않습니다."),
    NOT_FOUND_POST_ID(1003, "요청한 ID에 해당하는 게시글이 존재하지 않습니다."),
    NOT_FOUND_COMMENT_ID(1004, "요청한 ID에 해당하는 댓글이 존재하지 않습니다."),
    NOT_FOUND_IMAGE_ID(1005, "요청한 ID에 해당하는 이미지가 존재하지 않습니다."),
    NOT_FOUND_CHAT_ROOM_ID(1006, "요청한 ID에 해당하는 채팅방이 존재하지 않습니다."),
    NOT_FOUND_POST_WITH_MEMBER(1007, "요청한 유저 ID와 게시글 ID에 해당하는 게시글이 존재하지 않습니다."),
    NOT_FOUND_COMMENT_WITH_MEMBER(1008, "요청한 유저 ID와 댓글 ID에 해당하는 게시글이 존재하지 않습니다."),

    SHOULD_EXCEED_14_DAYS_TO_DELETE(2001, "카테고리를 삭제하려면 생성 후 14일이 지나야 합니다."),
    DUPLICATED_LOGIN_ID(2002, "이미 존재하는 로그인 아이디입니다.");

    private final int code;
    private final String message;
}
