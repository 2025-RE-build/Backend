package com.likelion.rebuild.domain.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionDto {

    private Long userMissionId;
    private String content;
    private boolean completed;
}
