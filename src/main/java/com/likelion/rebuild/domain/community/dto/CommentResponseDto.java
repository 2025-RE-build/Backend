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
    private Long postId;

    public CommentResponseDto(Comment c) {
        this.id = c.getId();
        this.content = c.getContent();
        this.author = ( c.getAuthor() != null) ? c.getAuthor().getNickname() : "알 수 없음";
        this.createdAt = c.getCreatedAt();
        this.postId = c.getPost().getId();
    }
}

