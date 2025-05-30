package com.recipAI.server.common.exception;

import com.recipAI.server.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import static com.recipAI.server.common.response.BaseResponseStatus.IMAGE_BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class RecipAIControllerAdvice {

    @ExceptionHandler(RecipAIException.class)
    public ResponseEntity<BaseResponse> handleKuchatException(RecipAIException e) {
        BaseResponse response = e.getResponse();
        log.error(String.valueOf(response));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<BaseResponse> handleMultipartException() {
        log.error("[handleMultipartException] error message = {}", IMAGE_BAD_REQUEST.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse(IMAGE_BAD_REQUEST, null));
    }
}
