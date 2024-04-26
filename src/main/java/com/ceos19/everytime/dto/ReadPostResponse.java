package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Attachment;
import com.ceos19.everytime.domain.AttachmentType;
import com.ceos19.everytime.domain.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Getter
@AllArgsConstructor
public class ReadPostResponse {
    private Long id;
    private String title;
    private String content;
    private Boolean isQuestion;
    private Boolean isAnonymous;
    private String authorName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<AttachmentDto> attachments;

    public static ReadPostResponse from(Post post) {
        List<AttachmentDto> attachmentDtos = new ArrayList<>();
        for (Attachment attachment : post.getAttachments()) {
            attachmentDtos.add(AttachmentDto.from(attachment));
        }

        return new ReadPostResponse(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.isQuestion(),
                post.isAnonymous(),
                post.getAuthor().getName(),
                post.getCreatedDate(),
                post.getModifiedDate(),
                attachmentDtos
        );
    }
}
