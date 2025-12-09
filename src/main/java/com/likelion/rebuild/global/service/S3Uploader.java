package com.likelion.rebuild.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일 업로드
     */
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile);   // Multipart → File 변환
        String key = dirName + "/" + createFileName(multipartFile.getOriginalFilename());

        amazonS3.putObject(new PutObjectRequest(bucket, key, uploadFile));


        removeTempFile(uploadFile);

        // 업로드된 URL 반환
        return amazonS3.getUrl(bucket, key).toString();
    }

    /**
     * MultipartFile → File 변환
     * (임시 파일 생성)
     */
    private File convert(MultipartFile file) throws IOException {
        File convertFile = File.createTempFile("temp-", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }
        return convertFile;
    }

    /**
     * 임시 파일 삭제
     */
    private void removeTempFile(File file) {
        if (file.delete()) {
            log.info("임시 파일 삭제 성공: " + file.getName());
        } else {
            log.warn("임시 파일 삭제 실패: " + file.getName());
        }
    }

    /**
     * 고유 파일 이름 생성 (UUID + original name)
     */
    private String createFileName(String originalName) {
        String cleanName = originalName.replaceAll("\\s", "_");
        return UUID.randomUUID() + "_" + cleanName;
    }

    /**
     * 파일 삭제
     * 전달받은 URL을 key로 변환한 뒤 삭제
     */
    public void deleteFile(String fileUrl) {

        try {
            String key = extractKeyFromUrl(fileUrl);

            amazonS3.deleteObject(bucket, key);
            log.info("S3 파일 삭제 성공: {}", key);

        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: URL={}, cause={}", fileUrl, e.getMessage());
        }
    }

    /**
     * S3 URL → Object Key 변환
     * https://bucket.s3.region.amazonaws.com/folder/uuid_file.png
     * → folder/uuid_file.png 로 변환
     */
    private String extractKeyFromUrl(String url) {
        int idx = url.indexOf(".com/");

        if (idx == -1) {
            throw new IllegalArgumentException("URL 형식 오류: " + url);
        }

        return url.substring(idx + 5); // ".com/" 이후 문자열 반환
    }
}
