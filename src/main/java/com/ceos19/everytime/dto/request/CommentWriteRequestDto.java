package com.ceos19.everytime.dto.request;

public record CommentWriteRequestDto(Long postId,
                                     Long writerId,
                                     Long parentCommentId,
                                     String content,
                                     boolean isAnonymous) {
}
