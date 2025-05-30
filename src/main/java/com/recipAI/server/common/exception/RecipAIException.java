package com.recipAI.server.common.exception;

import com.recipAI.server.common.response.BaseResponse;
import com.recipAI.server.common.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class RecipAIException extends RuntimeException {

    private final BaseResponse response;

    public RecipAIException(BaseResponseStatus responseStatus) {
        response = new BaseResponse(responseStatus, null);
    }

    public RecipAIException(BaseResponseStatus responseStatus, String message) {
        response = new BaseResponse(responseStatus, message, null);
        response.setMessage(message);
    }
}
