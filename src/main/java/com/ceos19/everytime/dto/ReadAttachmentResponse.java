package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Attachment;
import com.ceos19.everytime.domain.AttachmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.PrivateKey;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Getter
@AllArgsConstructor
public class ReadAttachmentResponse {
    private Long id;
    private String originalFileName;
    private String storedPath;
    private AttachmentType attachmentType;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public static ReadAttachmentResponse from(Attachment attachment) {
        return new ReadAttachmentResponse(attachment.getId(),
                attachment.getOriginalFileName(),
                attachment.getStoredPath(),
                attachment.getAttachmentType(),
                attachment.getCreatedDate(),
                attachment.getModifiedDate());
    }
}
