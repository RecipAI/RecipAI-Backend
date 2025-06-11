package com.recipAI.server.domain.chat;

import com.recipAI.server.common.exception.RecipAIException;
import com.recipAI.server.common.s3.S3Service;
import com.recipAI.server.domain.chat.dto.IngredientsResponse;
import com.recipAI.server.domain.chat.dto.IngredientsStringRequest;
import com.recipAI.server.domain.chat.dto.MenusResponse;
import com.recipAI.server.domain.chat.dto.RecipeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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
    public ResponseEntity<IngredientsResponse> detectIngredients(@RequestPart("image") MultipartFile image) {
        log.info("[detectIngredients] 재료 이미지 요청");
        String imageUrl = uploadImage(image);
        log.info("[detectIngredients] imageUrl = {}", imageUrl);
        IngredientsResponse response = chatService.requestIngredients(imageUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/menus")
    public ResponseEntity<MenusResponse> getMenus(@RequestBody List<String> ingredients) {
        log.info("[getMenus] 재료 목록을 통한 요리 레시피 요청. 재료 목록 = {}", ingredients);
        MenusResponse response = chatService.requestMenus(ingredients);
        log.info("[getMenus] GPT 응답 = {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recipe")
    public ResponseEntity<RecipeResponse> getRecipe(@RequestBody String menuName) {
        log.info("[getRecipe] 사용자가 선택한 메뉴의 레시피 요청. 메뉴 이름 = {}", menuName);
        RecipeResponse response = chatService.requestRecipe(menuName);
        log.info("[getRecipe] GPT 응답 = {}", response);
        return ResponseEntity.ok(response);
    }

    private String uploadImage(MultipartFile image) throws RecipAIException {
        try {
            return s3Service.uploadImage(image);
        } catch (IOException e) {
            throw new RecipAIException(IMAGE_UPLOAD_FAIL);
        }
    }

    private String toBase64DataUrl(MultipartFile file) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(file.getBytes());
        String mimeType = file.getContentType(); // 예: image/png
        return "data:" + mimeType + ";base64," + base64;
    }

}
