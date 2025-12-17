package com.likelion.rebuild.domain.message.controller;

import com.likelion.rebuild.domain.message.dto.ListResponseDto;
import com.likelion.rebuild.domain.message.dto.MessageResponseDto;
import com.likelion.rebuild.domain.message.entity.Message;
import com.likelion.rebuild.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monologues")
public class ListController {

    private final MessageService messageService;

    // 혼잣말 리스트
    @GetMapping
    public List<ListResponseDto> list() {
        return messageService.getMonologueList();
    }

    // 혼잣말 상세
    @GetMapping("/{monologueId}")
    public List<MessageResponseDto> detail(@PathVariable Long monologueId) {
        return messageService.getMonologue(monologueId);
    }
}

