package com.likelion.rebuild.domain.breakup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BreakupSaveRequestDto {

    // 상대방과 헤어진 이유
    private String reason;

    // 언제 헤어졌는지 (예: 2025-11-10)
    private LocalDate breakupDate;
}
