package com.likelion.rebuild.domain.message.repository;

import com.likelion.rebuild.domain.message.dto.ListResponseDto;
import com.likelion.rebuild.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 혼잣말 리스트
    @Query("""
select new com.likelion.rebuild.domain.message.dto.ListResponseDto(
    m.monologueId,
    min(m.createdAt),
    min(m.content)
)
from Message m
group by m.monologueId
order by min(m.createdAt) desc
""")
    List<ListResponseDto> findMonologueList();



    // 특정 혼잣말 내용
    List<Message> findByMonologueIdOrderByCreatedAtAsc(Long monologueId);
}

