package com.likelion.rebuild.domain.support.controller;

import com.likelion.rebuild.domain.support.dto.SupportMessageResponseDto;
import com.likelion.rebuild.domain.support.service.SupportMessageService;
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
@RequestMapping("/api/support-messages")
@RequiredArgsConstructor
public class SupportMessageController {

    private final SupportMessageService supportMessageService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    // 홈 화면에서 호출
    @GetMapping("/today")
    public ResponseEntity<SupportMessageResponseDto> getTodayMessage(
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        SupportMessageResponseDto dto = supportMessageService.getTodaySupportMessage(userId);
        return ResponseEntity.ok(dto);
    }
}
