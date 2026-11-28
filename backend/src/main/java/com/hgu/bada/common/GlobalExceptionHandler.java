package com.hgu.bada.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.stream.Collectors;

// 모든 Controller에서 던져진 예외를 한 곳에서 받아 ErrorResponse로 바꾼다
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 서비스가 직접 던진 예외 (404 없음, 409 중복)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus().value(), e.getCode(), e.getMessage()));
    }

    // @Valid 검증 실패 (예: 이름이 비었음, 파고 25m) → 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return badRequest("VALIDATION_FAILED", message);
    }

    // JSON 형식이 깨졌거나 enum 값이 틀림 (예: region = "SEOUL") → 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException e) {
        return badRequest("INVALID_REQUEST_BODY", "요청 본문 형식이 올바르지 않습니다.");
    }

    // 쿼리 파라미터 타입이 틀림 (예: ?region=SEOUL, /api/beaches/abc) → 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return badRequest("INVALID_PARAMETER", e.getName() + " 값이 올바르지 않습니다: " + e.getValue());
    }

    private ResponseEntity<ErrorResponse> badRequest(String code, String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(400, code, message));
    }
}
