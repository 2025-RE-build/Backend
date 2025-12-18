package com.likelion.rebuild.domain.user.controller;

import com.likelion.rebuild.domain.user.dto.LoginRequestDto;
import com.likelion.rebuild.domain.user.dto.SignUpRequestDto;
import com.likelion.rebuild.domain.user.dto.TokenResponseDto;
import com.likelion.rebuild.domain.user.service.UserAuthService;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import com.likelion.rebuild.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
            @RequestBody SignUpRequestDto request
    ) {
        userAuthService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody LoginRequestDto request
    ) {
        TokenResponseDto tokens = userAuthService.login(request);

        ResponseCookie refreshCookie =
                CookieUtil.createRefreshTokenCookie(tokens.getRefreshToken());

        TokenResponseDto body = new TokenResponseDto(tokens.getAccessToken(), null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND);
        }

        TokenResponseDto tokens = userAuthService.refreshByToken(refreshToken);

        ResponseCookie refreshCookie =
                CookieUtil.createRefreshTokenCookie(tokens.getRefreshToken());

        TokenResponseDto body = new TokenResponseDto(tokens.getAccessToken(), null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie deleteCookie = CookieUtil.deleteRefreshTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
}
