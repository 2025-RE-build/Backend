package com.likelion.rebuild.domain.mission.service;

import com.likelion.rebuild.domain.breakup.entity.BreakupEvent;
import com.likelion.rebuild.domain.breakup.repository.BreakupEventRepository;
import com.likelion.rebuild.domain.mission.dto.MissionDto;
import com.likelion.rebuild.domain.mission.entity.Mission;
import com.likelion.rebuild.domain.mission.entity.UserMission;
import com.likelion.rebuild.domain.mission.repository.MissionRepository;
import com.likelion.rebuild.domain.mission.repository.UserMissionRepository;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final UserRepository userRepository;
    private final BreakupEventRepository breakupEventRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private BreakupEvent getBreakupOrThrow(User user) {
        return breakupEventRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.BREAKUP_EVENT_NOT_FOUND));
    }

    // 오늘의 미션 3개: BreakupEvent.createdAt 기준 7일 동안만 노출
    @Transactional
    public List<MissionDto> getTodayMissions(Long userId) {
        User user = getUserOrThrow(userId);
        BreakupEvent breakup = getBreakupOrThrow(user);

        LocalDate today = LocalDate.now();

        LocalDate startDate;
        if (breakup.getCreatedAt() != null) {
            startDate = breakup.getCreatedAt().toLocalDate();
        } else {
            startDate = breakup.getBreakupDate();
        }

        long daysSinceStart = ChronoUnit.DAYS.between(startDate, today);

        // 0~6일동안 미션
        if (daysSinceStart < 0 || daysSinceStart >= 7) {
            return List.of();
        }

        List<UserMission> todays = userMissionRepository.findByUserAndMissionDate(user, today);

        if (todays.size() < 3) {
            fillTodayMissions(user, breakup, today, todays);
        }

        return todays.stream()
                .map(um -> new MissionDto(
                        um.getId(),
                        um.getMission().getContent(),
                        um.isCompleted()
                ))
                .toList();
    }

    private void fillTodayMissions(User user,
                                   BreakupEvent breakup,
                                   LocalDate today,
                                   List<UserMission> todays) {

        LocalDate start = breakup.getCreatedAt() != null
                ? breakup.getCreatedAt().toLocalDate()
                : breakup.getBreakupDate();
        LocalDate end = start.plusDays(6);

        List<Long> usedMissionIds = userMissionRepository
                .findMissionIdsByUserAndMissionDateBetween(user, start, end);

        List<Mission> candidates = missionRepository.findAll();

        candidates = candidates.stream()
                .filter(m -> !usedMissionIds.contains(m.getId()))
                .collect(Collectors.toList());

        Collections.shuffle(candidates);

        int need = 3 - todays.size();

        for (int i = 0; i < need && i < candidates.size(); i++) {
            Mission mission = candidates.get(i);

            UserMission um = UserMission.builder()
                    .user(user)
                    .mission(mission)
                    .missionDate(today)
                    .completed(false)
                    .build();

            userMissionRepository.save(um);
            todays.add(um);
        }
    }

    // 미션 완료 체크
    @Transactional
    public void completeMission(Long userId, Long userMissionId) {
        User user = getUserOrThrow(userId);

        UserMission um = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_MISSION_NOT_FOUND));

        if (!um.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_MISSION_ACCESS);
        }

        um.complete();
    }
}
