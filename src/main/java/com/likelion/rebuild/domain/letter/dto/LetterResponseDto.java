package com.likelion.rebuild.domain.letter.dto;

import com.likelion.rebuild.domain.letter.entity.Letter;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LetterResponseDto {

    private final Long id;
    private final String toName;
    private final String fromName;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public LetterResponseDto(Letter letter) {
        this.id = letter.getId();;
        this.toName = letter.getToName();
        this.fromName = letter.getFromName();
        this.content = letter.getContent();
        this.createdAt = letter.getCreatedAt();
        this.updatedAt = letter.getUpdatedAt();
    }
}
