package com.likelion.rebuild.domain.home.dto;

import com.likelion.rebuild.domain.breakup.dto.BreakupHomeResponseDto;
import com.likelion.rebuild.domain.mission.dto.MissionDto;
import com.likelion.rebuild.domain.support.dto.SupportMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponseDto {

    private BreakupHomeResponseDto breakup;

    private List<MissionDto> missions;

    private SupportMessageResponseDto supportMessage;
}
