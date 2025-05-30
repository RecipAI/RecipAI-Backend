package com.recipAI.server.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private final boolean isSuccess;
    private final int code;
    private String message;
    private final T result;

    public BaseResponse(BaseResponseStatus responseStatus, T result) {
        this.isSuccess = responseStatus.isSuccess();
        this.code = responseStatus.getCode();
        this.message = responseStatus.getMessage();
        this.result = result;
    }

    public BaseResponse(BaseResponseStatus responseStatus, String message, T result) {
        this.isSuccess = responseStatus.isSuccess();
        this.code = responseStatus.getCode();
        this.message = message;
        this.result = result;
    }
}
