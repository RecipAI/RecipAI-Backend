package com.recipAI.server.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class SimpleErrorResponse <T> extends BaseResponse {

    public SimpleErrorResponse(BaseResponseStatus responseStatus) {
        super(responseStatus, responseStatus.getMessage());
    }

    public SimpleErrorResponse (BaseResponseStatus responseStatus, String message, T result) {
        super(responseStatus, message,  result);
    }
}
