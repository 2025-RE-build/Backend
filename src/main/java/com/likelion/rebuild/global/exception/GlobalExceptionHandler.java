package com.likelion.rebuild.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ======= 커스텀 비즈니스 예외 =======
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("[CustomException] {} - {}", errorCode.getCode(), ex.getMessage());

        ErrorResponse body = ErrorResponse.of(errorCode, request.getRequestURI());
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    // ======= @Valid 검증 실패 (Body) =======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .orElse("유효하지 않은 요청 값입니다.");

        log.warn("[MethodArgumentNotValidException] {}", msg);

        ErrorResponse body = ErrorResponse.of(
                ErrorCode.INVALID_INPUT_VALUE,
                request.getRequestURI(),
                msg
        );
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus()).body(body);
    }

    // ======= @Valid 검증 실패 (Query, Path 등) =======
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex,
            HttpServletRequest request
    ) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .orElse("유효하지 않은 요청 값입니다.");

        log.warn("[BindException] {}", msg);

        ErrorResponse body = ErrorResponse.of(
                ErrorCode.INVALID_INPUT_VALUE,
                request.getRequestURI(),
                msg
        );
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus()).body(body);
    }

    // ======= JSON 파싱 실패 =======
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        log.warn("[HttpMessageNotReadableException] {}", ex.getMessage());
        ErrorResponse body = ErrorResponse.of(
                ErrorCode.INVALID_INPUT_VALUE,
                request.getRequestURI(),
                "JSON 형식이 올바르지 않습니다."
        );
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus()).body(body);
    }

    // ======= 지원하지 않는 HTTP Method =======
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        log.warn("[HttpRequestMethodNotSupportedException] {}", ex.getMessage());
        ErrorResponse body = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED, request.getRequestURI());
        return ResponseEntity.status(ErrorCode.METHOD_NOT_ALLOWED.getStatus()).body(body);
    }

    // ======= 그 외 모든 예외 (마지막 방어선) =======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("[Exception] {}", ex.getMessage(), ex);
        ErrorResponse body = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(body);
    }
}
