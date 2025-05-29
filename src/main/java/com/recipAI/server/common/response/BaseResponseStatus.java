package com.recipAI.server.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum BaseResponseStatus {
    // 1000 번대 : global 요청 성공/실패
    SUCCESS(1000, HttpStatus.OK, "요청에 성공하였습니다."),

    PARAMETER_NOT_FOUND(1010, HttpStatus.BAD_REQUEST, "요청 url의 쿼리 파라미터가 누락됐습니다."),
    PATH_VARIABLE_NOT_FOUND(1011, HttpStatus.BAD_REQUEST, "요청 url의 path variable이 누락됐습니다."),
    API_NOT_FOUND(1012, HttpStatus.NOT_FOUND, "잘못된 요청입니다. 요청 URL을 확인해주세요."),

    //- 2000 번대 : 사용자가 첨부한 재료 이미지 관련 코드
    IMAGE_BAD_REQUEST(2001, HttpStatus.BAD_REQUEST, "이미지 업로드 요청 방식이 올바르지 않습니다. 다시 시도해주세요."),
    IMAGE_UPLOAD_FAIL(2002, HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다. 다시 시도해주세요."),
    NOT_FOUND_IMAGE(2003, HttpStatus.BAD_REQUEST, "요청에서 이미지 파일을 찾을 수 없습니다. 다시 시도해주세요."),
    OVER_SIZE_IMAGE(2004, HttpStatus.BAD_REQUEST, "이미지 파일이 용량을 초과하여 업로드할 수 없습니다. 업로드 가능한 크기는 최대 10MB 입니다."),

    //- 9000번대 : DB 관련 코드
    DB_SUCCESS(8000, HttpStatus.ACCEPTED,"DB에 성공적으로 반영되었습니다."),
    DB_SAVE_FAIL(8001, HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장에 실패했습니다.");

    private int code;
    private HttpStatus httpStatus;
    @Setter
    private String message;
}
