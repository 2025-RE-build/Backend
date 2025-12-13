package com.likelion.rebuild.domain.message.service;

import com.likelion.rebuild.domain.message.dto.ListResponseDto;
import com.likelion.rebuild.domain.message.dto.MessageRequestDto;
import com.likelion.rebuild.domain.message.dto.MessageResponseDto;
import com.likelion.rebuild.domain.message.entity.Message;
import com.likelion.rebuild.domain.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repo;

    // WebSocket / REST 공용 저장
    public MessageResponseDto save(MessageRequestDto req) {
        Message m = new Message(req.getMonologueId(), req.getContent());
        Message saved = repo.save(m);
        return new MessageResponseDto(saved);
    }

    // 혼잣말 리스트
    public List<ListResponseDto> getMonologueList() {
        return repo.findMonologueList().stream()
                .map(row -> new ListResponseDto(
                        (Long) row[0],
                        (LocalDateTime) row[1],
                        (String) row[2]
                ))
                .toList();
    }

    // 혼잣말 상세
    public List<Message> getMonologue(Long monologueId) {
        return repo.findByMonologueIdOrderByCreatedAtAsc(monologueId);
    }
}
