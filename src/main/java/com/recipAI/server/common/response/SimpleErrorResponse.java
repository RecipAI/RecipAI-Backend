package com.recipAI.server.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SimpleErrorResponse extends BaseResponse {

    public SimpleErrorResponse(BaseResponseStatus responseStatus) {
        super(responseStatus);
    }
}
