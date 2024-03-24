package com.ceos19.everyTime.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostImageService {
    private final AmazonS3Client amazonS3Client;
    private final static String[] extensionArr={"jpg","jpeg","bmp","gif","png"};


    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucketUrl}")
    private String url;


    //s3 저장
    public String saveImage(MultipartFile file, String directoryName,String originalName) {
        try{
            ObjectMetadata metadata=new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getInputStream().available());


            String storedName= generateStoreName(originalName);

            amazonS3Client.putObject(new PutObjectRequest(bucketName,directoryName+storedName,file.getInputStream(),metadata).withCannedAcl(
                CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucketName,directoryName+storedName).toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //s3 삭제
    public void deleteImage(String fileRoute) {

        int index = fileRoute.indexOf(url);
        String fileName = fileRoute.substring(index + url.length() + 1);
        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName, fileName);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucketName, fileName);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    //확장자 추출
    public String extractExtension(String originalName){
        int index=originalName.lastIndexOf('.');
        return originalName.substring(index+1);
    }

    //UUID 통한 accessUrl 생성 및 체크
    public String generateStoreName(String originalName){
        String extension=extractExtension(originalName);
        if(!checkValidation(extension)) throw new RuntimeException(extension+" 은 지원하지 않는 확장자입니다");
        return UUID.randomUUID()+"."+extension;
    }
    //이미지 파일의 확장자를 통하여 유효한 이미지 파일인지 확인하는 메서드
    public boolean checkValidation(String extension){
        return Arrays.stream(extensionArr).anyMatch(value->value.equals(extension));
    }





}
