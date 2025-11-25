package com.likelion.rebuild.domain.message.service;

import com.likelion.rebuild.domain.message.dto.MessageRequestDto;
import com.likelion.rebuild.domain.message.dto.MessageResponseDto;
import com.likelion.rebuild.domain.message.entity.Message;
import com.likelion.rebuild.domain.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repo;

    public MessageResponseDto save(MessageRequestDto req) {
        Message m = new Message(req.getContent());
        Message saved = repo.save(m);
        return new MessageResponseDto(saved);
    }

    public List<MessageResponseDto> list() {
        return repo.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(MessageResponseDto::new)
                .toList();
    }

    public MessageResponseDto saveContent(String content) {
        Message m = new Message(content);
        Message saved = repo.save(m);
        return new MessageResponseDto(saved);
    }

}
