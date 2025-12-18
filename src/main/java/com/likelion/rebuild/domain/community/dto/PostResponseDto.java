package com.likelion.rebuild.domain.community.dto;

import com.likelion.rebuild.domain.community.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;

    public PostResponseDto(Post p) {
        this.id = p.getId();
        this.title = p.getTitle();
        this.content = p.getContent();
        this.author = (p.getAuthor() != null) ? p.getAuthor().getNickname() : "알 수 없음";
        this.createdAt = p.getCreatedAt();
        this.updatedAt = p.getUpdatedAt();
        this.imageUrl = p.getImageUrl();
    }
}

