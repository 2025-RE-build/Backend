package com.likelion.rebuild.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ========= USER =========
    USER_LOGIN_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "USER001", "이미 존재하는 아이디입니다."),
    USER_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "USER002", "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER003", "존재하지 않는 사용자입니다."),
    USER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "USER004", "비밀번호가 일치하지 않습니다."),

    // ========= AUTH / TOKEN =========
    AUTH_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH001", "Refresh Token이 없습니다."),
    AUTH_REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH002", "유효하지 않은 Refresh Token입니다."),
    AUTH_REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "AUTH003", "저장된 Refresh Token과 일치하지 않습니다."),
    AUTH_ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH004", "접근 권한이 없습니다."),

    // ========= COMMON =========
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON001", "요청 값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON002", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON999", "서버 내부 에러가 발생했습니다."),

    // ========= LETTER =========
    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER001", "존재하지 않는 편지입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
