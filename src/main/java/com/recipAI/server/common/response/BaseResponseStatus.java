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
    UNSUPPORTED_FILE_FORMAT(2003, HttpStatus.BAD_REQUEST, "지원되지 않는 파일 형식입니다. PNG, JPEG, WEBP 또는 애니메이션이 없는 GIF 형식을 사용해주세요."),
    OVER_SIZE_IMAGE(2004, HttpStatus.BAD_REQUEST, "이미지 파일이 용량을 초과하여 업로드할 수 없습니다. 업로드 가능한 크기는 최대 50MB 입니다."),
    NOT_FOUND_IMAGE(2005, HttpStatus.BAD_REQUEST, "요청에서 이미지 파일을 찾을 수 없습니다. 다시 시도해주세요."),

    //- 3000번대 : open ai 통신 관련 코드
    OPENAI_API_ERROR(3001, HttpStatus.BAD_REQUEST, "open AI API를 사용하는 과정에서 오류가 발생했습니다."),
    INVALID_REQUEST_ERROR(3002, HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었거나, 요청 형식이 올바르지 않습니다."),        // 입력 항목 누락 또는 형식 오류 (json 파싱 오류)
    INVALID_MODEL(3003, HttpStatus.BAD_REQUEST, "존재하지 않거나 허용되지 않는 모델입니다."),
    CONTEXT_LENGTH_EXCEEDED(3004, HttpStatus.BAD_REQUEST, "메시지 토큰 수가 모델의 컨텍스트 길이를 초과했습니다."),
    RATE_LIMIT_ERROR(3005, HttpStatus.BAD_REQUEST, "너무 많은 요청을 너무 빠르게 보내서 오류가 발생했습니다."),
    INVALID_API_KEY(3006, HttpStatus.UNAUTHORIZED, "OPEN AI API 키 형식이 유효하지 않습니다."),
    MISSING_API_KEY(3007, HttpStatus.UNAUTHORIZED, "요청 헤더에 API 키가 포함되지 않았습니다."),
    INSUFFICIENT_QUOTA(3008, HttpStatus.UNAUTHORIZED, "사용자의 API 크레딧 또는 사용량을 초과했습니다."),


    //- 3000번대 : DB 관련 코드
    DB_SUCCESS(4000, HttpStatus.ACCEPTED,"DB에 성공적으로 반영되었습니다."),
    DB_SAVE_FAIL(4001, HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장에 실패했습니다.");

    private int code;
    private HttpStatus httpStatus;
    @Setter
    private String message;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
