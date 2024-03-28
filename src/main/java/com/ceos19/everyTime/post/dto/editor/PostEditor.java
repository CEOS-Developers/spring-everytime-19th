package com.ceos19.everyTime.post.dto.editor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PostEditor {
    private final String title;
    private final String content;
    private final boolean isQuestion;
    private final boolean hideNickName;

    @Builder
    public PostEditor(String title,String content,boolean isQuestion,boolean hideNickName){
        this.title= title;
        this.content=content;
        this.isQuestion = isQuestion;
        this.hideNickName = hideNickName;

    }

}
