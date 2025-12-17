package com.likelion.rebuild.domain.message.dto;

import com.likelion.rebuild.domain.message.entity.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {

    private Long id;
    private Long monologueId;
    private String content;
    private LocalDateTime createdAt;

    public MessageResponseDto(Message message) {
        this.id = message.getId();
        this.monologueId = message.getMonologueId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
    }
}
