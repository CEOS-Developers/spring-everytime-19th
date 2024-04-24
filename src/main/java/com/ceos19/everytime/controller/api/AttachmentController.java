package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Attachment;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @DeleteMapping("/{attachment_id}")
    public ResponseEntity<BaseResponse> deleteAttachment(@PathVariable("attachment_id") Long attachmentId) {
        try {
            attachmentService.removeAttachment(attachmentId);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
