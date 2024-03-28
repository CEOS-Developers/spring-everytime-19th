package com.ceos19.everyTime.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyCommentSaveDto {

    private String comment;

    private Long parentId;

    private boolean hideNickName;

    @Builder
    public ReplyCommentSaveDto(String comment,Long parentId, boolean hideNickName){
        this.comment = comment;
        this.parentId = parentId;
        this.hideNickName = hideNickName;
    }

}
