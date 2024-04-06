package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Attachment;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddPostRequest {
    @NotNull
    private Long userId;  // request body 에 사용자 정보 포함

    @NotNull
    private String title;
    private String content;

    private boolean isQuestion;
    private boolean isAnonymous;

    private List<AttachmentDto> attachments = new ArrayList<>();
}
