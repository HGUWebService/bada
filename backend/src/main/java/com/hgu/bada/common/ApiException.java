package com.hgu.bada.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 서비스에서 "이 요청은 이런 이유로 실패"를 알릴 때 던지는 예외
// 예: throw ApiException.notFound("BEACH_NOT_FOUND", "해변을 찾을 수 없습니다. id=3");
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status; // 응답 HTTP 상태 (404, 409 ...)
    private final String code;       // 프론트가 분기할 때 쓰는 오류 코드

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static ApiException notFound(String code, String message) {
        return new ApiException(HttpStatus.NOT_FOUND, code, message);
    }

    public static ApiException conflict(String code, String message) {
        return new ApiException(HttpStatus.CONFLICT, code, message);
    }
}
