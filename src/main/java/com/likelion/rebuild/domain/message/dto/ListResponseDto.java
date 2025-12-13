package com.likelion.rebuild.domain.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ListResponseDto {

    private Long monologueId;
    private LocalDateTime createdAt;
    private String preview;
}
