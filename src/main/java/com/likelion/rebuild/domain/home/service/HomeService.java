package com.likelion.rebuild.domain.home.service;

import com.likelion.rebuild.domain.breakup.dto.BreakupHomeResponseDto;
import com.likelion.rebuild.domain.breakup.service.BreakupService;
import com.likelion.rebuild.domain.home.dto.HomeResponseDto;
import com.likelion.rebuild.domain.mission.dto.MissionDto;
import com.likelion.rebuild.domain.mission.service.MissionService;
import com.likelion.rebuild.domain.support.dto.SupportMessageResponseDto;
import com.likelion.rebuild.domain.support.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final BreakupService breakupService;
    private final MissionService missionService;
    private final SupportMessageService supportMessageService;

    @Transactional(readOnly = true)
    public HomeResponseDto getHome(Long userId) {

        BreakupHomeResponseDto breakupSummary = breakupService.getHomeSummary(userId);

        List<MissionDto> missions = missionService.getTodayMissions(userId);

        SupportMessageResponseDto supportMessage =
                supportMessageService.getTodaySupportMessage(userId);

        return new HomeResponseDto(
                breakupSummary,
                missions,
                supportMessage
        );
    }
}
