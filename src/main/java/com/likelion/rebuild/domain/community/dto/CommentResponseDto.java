package com.likelion.rebuild.domain.community.dto;

import com.likelion.rebuild.domain.community.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment c) {
        this.id = c.getId();
        this.content = c.getContent();
        this.author = c.getAuthor().getNickname();
        this.createdAt = c.getCreatedAt();
    }
}

