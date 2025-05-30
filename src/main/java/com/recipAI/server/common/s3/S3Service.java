package com.recipAI.server.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.recipAI.server.common.exception.RecipAIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.recipAI.server.common.response.BaseResponseStatus.IMAGE_BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final String DIR_NAME = "/ingredients";

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        log.info("[uploadImage] 이미지 업로드 요청");
        File file = convertToFile(multipartFile)
                .orElseThrow(() -> new RecipAIException(IMAGE_BAD_REQUEST));
        return putS3(file);
    }

    private String putS3(File file) {
        String fileName = createFilePath(file);
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
        file.delete();
        String imageUrl = amazonS3.getUrl(bucketName, fileName).toString();
        log.info("[putS3] image url = {}", imageUrl);
        return imageUrl;
    }

    private String createFilePath(File file) {
        LocalDateTime uploadTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        String formattedUploadTime = uploadTime.format(formatter);
        String fileName = getFileName(file);

        return DIR_NAME + "/" + formattedUploadTime + "_" + fileName;
    }

    private String getFileName(File file) {
        String sanitizeFileName = file.getName().replaceAll("[^a-zA-Z0-9_-]", "");
        if (sanitizeFileName.isBlank() || sanitizeFileName.length() < 5) {
            sanitizeFileName = UUID.randomUUID().toString();
        }
        return sanitizeFileName;
    }

    private Optional<File> convertToFile(MultipartFile multipartFile) throws IOException {
        log.info("[converToFile] multipartFile을 File로 변경");
        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(multipartFile.getBytes());
        }
        return Optional.of(convertFile);
    }


    public void deleteImage(String imageUrl) {
        log.info("[deleteImage] 우리 서버의 S3 올라가있는 기존 이미지만 삭제");
        if (imageUrl.startsWith("https://" + bucketName + ".s3")) {
            String splitStr = "./com";
            String fileName = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        }
    }
}
