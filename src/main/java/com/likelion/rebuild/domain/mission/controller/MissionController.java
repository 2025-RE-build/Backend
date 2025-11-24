package com.likelion.rebuild.domain.mission.controller;

import com.likelion.rebuild.domain.mission.dto.MissionDto;
import com.likelion.rebuild.domain.mission.service.MissionService;
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
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }

    // 오늘의 미션 3개 조회
    @GetMapping("/today")
    public ResponseEntity<List<MissionDto>> getTodayMissions(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<MissionDto> missions = missionService.getTodayMissions(userId);
        return ResponseEntity.ok(missions);
    }

    // 미션 완료 체크
    @PostMapping("/{userMissionId}/complete")
    public ResponseEntity<Void> completeMission(
            @PathVariable Long userMissionId,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        missionService.completeMission(userId, userMissionId);
        return ResponseEntity.ok().build();
    }
}
