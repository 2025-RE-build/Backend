package com.likelion.rebuild.domain.message.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Message {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    // 혼잣말 묶음 ID
    @Column(nullable = false)
    private Long monologueId;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Message(Long monologueId, String content) {
        this.monologueId = monologueId;
        this.content = content;
    }
}
