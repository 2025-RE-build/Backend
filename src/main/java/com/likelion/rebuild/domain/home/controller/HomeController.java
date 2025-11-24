package com.likelion.rebuild.domain.home.controller;

import com.likelion.rebuild.domain.home.dto.HomeResponseDto;
import com.likelion.rebuild.domain.home.service.HomeService;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    // 홈 화면: 헤어진 이유 + D+N + 오늘의 미션 3개 + 응원 메시지
    @GetMapping
    public ResponseEntity<HomeResponseDto> getHome(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        HomeResponseDto home = homeService.getHome(userId);
        return ResponseEntity.ok(home);
    }
}
