package com.ceos19.everytime.utils;

import com.ceos19.everytime.domain.Attachment;
import com.ceos19.everytime.domain.AttachmentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtil {
    @Value("${file.dir}")
    private String fileDirPath;

    public List<Attachment> storeFiles(List<MultipartFile> multipartFiles, AttachmentType attachmentType) throws IOException {  // 전체 파일 저장
        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                attachments.add(storeFile(multipartFile, attachmentType));
            }
        }

        return attachments;
    }

    public Attachment storeFile(MultipartFile multipartFile, AttachmentType attachmentType) throws IOException {  // 파일 저장
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(createPath(storeFilename, attachmentType)));

        return Attachment.builder()
                .originFileName(originalFilename)
                .storePath(storeFilename)
                .attachmentType(attachmentType)
                .build();

    }

    public String createPath(String storeFilename, AttachmentType attachmentType) {  //  파일 경로 구성
        String viaPath = (attachmentType == AttachmentType.IMAGE) ? "images/" : "generals/";
        return fileDirPath+viaPath+storeFilename;
    }

    private String createStoreFilename(String originalFilename) {  // 저장 파일 이름 구성
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        String storeFilename = uuid + ext;

        return storeFilename;
    }

    private String extractExt(String originalFilename) {  // 확장자 추출 메서드
        int idx = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(idx);
        return ext;
    }

}
