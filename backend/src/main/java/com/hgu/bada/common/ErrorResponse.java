package com.hgu.bada.common;

import java.time.LocalDateTime;

// 모든 오류 응답의 공통 모양: {status, code, message, timestamp}
public record ErrorResponse(int status, String code, String message, LocalDateTime timestamp) {

    public static ErrorResponse of(int status, String code, String message) {
        return new ErrorResponse(status, code, message, LocalDateTime.now());
    }
}
