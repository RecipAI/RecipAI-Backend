package com.recipAI.server.common.exception;

import com.recipAI.server.common.response.BaseResponse;
import com.recipAI.server.common.response.BaseResponseStatus;
import com.recipAI.server.common.response.SimpleErrorResponse;

public class RecipAIException extends RuntimeException {

    private BaseResponse response;

    public RecipAIException(BaseResponse response) {
        this.response = response;
    }

    public RecipAIException(BaseResponseStatus responseStatus) {
        response = new SimpleErrorResponse(responseStatus);
    }
}
