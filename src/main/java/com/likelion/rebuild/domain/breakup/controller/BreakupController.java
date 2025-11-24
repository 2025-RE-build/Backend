package com.likelion.rebuild.domain.breakup.controller;

import com.likelion.rebuild.domain.breakup.dto.BreakupHomeResponseDto;
import com.likelion.rebuild.domain.breakup.dto.BreakupSaveRequestDto;
import com.likelion.rebuild.domain.breakup.service.BreakupService;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/breakups")
@RequiredArgsConstructor
public class BreakupController {
    private final BreakupService breakupService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    // SAVE 버튼 눌렀을 때 (생성 or 수정)
    @PostMapping
    public ResponseEntity<Void> saveBreakup(
            @RequestBody BreakupSaveRequestDto request,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        breakupService.saveOrUpdateBreakup(userId, request);
        return ResponseEntity.ok().build();
    }

    // 홈화면에서 D+N, 이유, 날짜 가져오기
    @GetMapping("/home-summary")
    public ResponseEntity<BreakupHomeResponseDto> getHomeSummary(
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        BreakupHomeResponseDto summary = breakupService.getHomeSummary(userId);
        return ResponseEntity.ok(summary);
    }
}
