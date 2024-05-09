package com.ceos19.everytime.service;

import com.ceos19.everytime.controller.api.AttachmentController;
import com.ceos19.everytime.domain.Attachment;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos19.everytime.exception.ErrorCode.DATA_ALREADY_EXISTED;
import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Transactional(readOnly = true)
    public Attachment findAttachmentById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId).orElseThrow(() -> {
            log.error("에러 내용: 파일 조회 실패 " +
                    "발생 원인: 존재하지 않는 Attachment의 PK로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 파일입니다");
        });
    }

    public void removeAttachment(Long attachmentId) {
        attachmentRepository.findById(attachmentId).orElseThrow(() -> {
            log.error("에러 내용: 파일 제거 실패 " +
                    "발생 원인: 존재하지 않는 Attachment의 PK로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 파일입니다");
        });

        attachmentRepository.deleteById(attachmentId);
    }
}
