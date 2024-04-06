package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Attachment;
import com.ceos19.everytime.domain.AttachmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
    private String originalFileName;
    private String storedPath;
    private AttachmentType attachmentType;

    public static AttachmentDto from(Attachment attachment) {
        return new AttachmentDto(
                attachment.getOriginalFileName(),
                attachment.getStoredPath(),
                attachment.getAttachmentType());
    }
}
