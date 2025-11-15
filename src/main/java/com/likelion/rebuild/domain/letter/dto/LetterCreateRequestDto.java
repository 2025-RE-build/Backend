package com.likelion.rebuild.domain.letter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LetterCreateRequestDto {

    // 선택 항목(null 허용)
    private String toName;

    // 필수 항목
    private String content;

    // 선택 항목(null 허용)
    private String fromName;
}
