package com.likelion.rebuild.global.util;

import org.springframework.http.ResponseCookie;

public class CookieUtil {
    private static final String REFRESH_TOKEN_NAME = "refreshToken";
    private static final long REFRESH_TOKEN_MAX_AGE = 7L * 24 * 60 * 60;

    private CookieUtil() {}

    public static ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .sameSite("Lax")
                .build();
    }

    public static ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
