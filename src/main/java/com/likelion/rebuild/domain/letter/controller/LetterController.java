package com.likelion.rebuild.domain.letter.controller;

import com.likelion.rebuild.domain.letter.dto.LetterCreateRequestDto;
import com.likelion.rebuild.domain.letter.dto.LetterResponseDto;
import com.likelion.rebuild.domain.letter.service.LetterService;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    // 편지 작성
    @PostMapping
    public ResponseEntity<LetterResponseDto> createLetter(
            @RequestBody LetterCreateRequestDto request,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        LetterResponseDto response = letterService.createLetter(userId, request);
        return ResponseEntity.ok(response);
    }

    // 내 편지 목록 조회
    @GetMapping
    public ResponseEntity<List<LetterResponseDto>> getMyLetters(
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        List<LetterResponseDto> letters = letterService.getMyLetters(userId);
        return ResponseEntity.ok(letters);
    }

    // 편지 수정
    @PutMapping("/{letterId}")
    public ResponseEntity<LetterResponseDto> updateLetter(
            @PathVariable Long letterId,
            @RequestBody LetterCreateRequestDto request,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        LetterResponseDto updated = letterService.updateLetter(letterId, userId, request);
        return ResponseEntity.ok(updated);
    }

    // 편지 삭제
    @DeleteMapping("/{letterId}")
    public ResponseEntity<Void> deleteLetter(
            @PathVariable Long letterId,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        letterService.deleteLetter(letterId, userId);
        return ResponseEntity.ok().build();
    }

    // 편지 상세 조회
    @GetMapping("/{letterId}")
    public ResponseEntity<LetterResponseDto> getLetterDetail(
            @PathVariable Long letterId,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        LetterResponseDto response = letterService.getLetterDetail(letterId, userId);
        return ResponseEntity.ok(response);
    }
}
