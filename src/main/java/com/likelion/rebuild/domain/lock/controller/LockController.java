package com.likelion.rebuild.domain.lock.controller;

import com.likelion.rebuild.domain.lock.dto.LockDto;
import com.likelion.rebuild.domain.lock.service.LockService;
import com.likelion.rebuild.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lock")
public class LockController {

    private final LockService lockService;

    @PostMapping("/set")
    public ResponseEntity<?> setLockCode(
            @AuthenticationPrincipal User user,
            @RequestBody LockDto request
    ) {
        lockService.setLockCode(user, request);
        return ResponseEntity.ok("비밀번호 설정 완료");
    }

    @PostMapping("/unlock")
    public ResponseEntity<?> unlock(
            @AuthenticationPrincipal User user,
            @RequestBody LockDto request
    ) {
        boolean success = lockService.unlock(user, request);

        if (success) {
            return ResponseEntity.ok("Unlocked");
        } else {
            return ResponseEntity.status(401).body("비밀번호가 틀렸습니다");
        }
    }
}