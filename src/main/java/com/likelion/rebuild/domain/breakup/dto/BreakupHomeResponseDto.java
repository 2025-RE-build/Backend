package com.likelion.rebuild.domain.breakup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BreakupHomeResponseDto {

    private String reason;
    private LocalDate breakupDate;
    private long daysSince; // 헤어진 지 며칠째인지 (예: 32)
    private String dDayText; // ex: "D+32".. 같은 문자열
}
