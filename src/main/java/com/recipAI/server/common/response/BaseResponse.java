package com.recipAI.server.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    protected BaseResponseStatus responseStatus;

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return responseStatus.getHttpStatus();
    }
}
