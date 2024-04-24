package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.AttachmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddAttachmentRequest {
    @NotBlank
    private String originalFileName;
    @NotBlank
    private String storedPath;
    @NotBlank
    private AttachmentType attachmentType;
}
