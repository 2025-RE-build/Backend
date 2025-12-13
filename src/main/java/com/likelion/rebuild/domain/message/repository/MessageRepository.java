package com.likelion.rebuild.domain.message.repository;

import com.likelion.rebuild.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 혼잣말 리스트
    @Query("""
        SELECT m.monologueId,
               MIN(m.createdAt),
               MIN(m.content)
        FROM Message m
        GROUP BY m.monologueId
        ORDER BY MIN(m.createdAt) DESC
    """)
    List<Object[]> findMonologueList();

    // 특정 혼잣말 내용
    List<Message> findByMonologueIdOrderByCreatedAtAsc(Long monologueId);
}

