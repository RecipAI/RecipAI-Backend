package com.recipAI.server.domain.chat;

import com.recipAI.server.common.exception.RecipAIException;
import com.recipAI.server.common.s3.S3Service;
import com.recipAI.server.domain.chat.dto.IngredientsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.recipAI.server.common.response.BaseResponseStatus.IMAGE_UPLOAD_FAIL;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
@RestController
public class ChatController {
    private final S3Service s3Service;
    private final ChatService chatService;

    @PostMapping("/ingredients")
    public ResponseEntity<IngredientsResponse> detectIngredients(@RequestPart(required = false) List<MultipartFile> uploadImages) {
        log.info("[detectIngredients] 재료 이미지 요청");
        List<String> imageUrls = uploadImages.stream()
                .map(this::uploadImage)
                .toList();
        IngredientsResponse response = chatService.requestIngredients(imageUrls);
        return ResponseEntity.ok(response);
    }

    private String uploadImage(MultipartFile image) throws RecipAIException {
        try {
            return s3Service.uploadImage(image);
        } catch (IOException e) {
            throw new RecipAIException(IMAGE_UPLOAD_FAIL);
        }
    }

}
