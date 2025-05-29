package com.recipAI.server.domain.chat;

import com.recipAI.server.common.exception.RecipAIException;
import com.recipAI.server.domain.chat.dto.IngredientsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RequestMapping("/chat")
@RestController
public class ChatController {
//    private final S3Service s3Service;
    private final ChatService chatService;

    @PostMapping("/ingredients")
    public ResponseEntity<IngredientsResponse> detectIngredients(@RequestParam("image") MultipartFile image) {
        String imageUrl = uploadImage(image);
        IngredientsResponse response = chatService.requestIngredients(imageUrl);
        return ResponseEntity.ok(response);
    }

    private String uploadImage(MultipartFile image) throws RecipAIException {
//        try {
//            return s3Service.uploadImage(image);
//        } catch (IOException e) {
//            throw new RecipAIException(IMAGE_UPLOAD_FAIL);
//        }
        return "";
    }

}
