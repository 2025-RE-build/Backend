package com.likelion.rebuild.domain.message.dto;

import com.likelion.rebuild.domain.message.entity.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {

    private String content;
    private LocalDateTime createdAt;

    public MessageResponseDto(Message m) {
        this.content = m.getContent();
        this.createdAt = m.getCreatedAt();
    }
}
