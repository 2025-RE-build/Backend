package com.likelion.rebuild.domain.message.controller;

import com.likelion.rebuild.domain.message.dto.MessageRequestDto;
import com.likelion.rebuild.domain.message.dto.MessageResponseDto;
import com.likelion.rebuild.domain.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Tag(name = "Chat/WebSocket", description = "WebSocket 기반 실시간 채팅 관련 설명")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    // 클라이언트가 /app/chat 으로 메시지를 보냄
    @MessageMapping("/chat")
    public void handleChat(MessageRequestDto dto) {

        // 1) DB에 저장
        MessageResponseDto saved = messageService.save(dto);

        // 2) 저장된 메시지를 모든 클라이언트에게 push
        messagingTemplate.convertAndSend("/topic/messages", saved);
    }


    // 🔥 [Swagger용 더미 API] - WebSocket 연결 정보 제공
    @Operation(
            summary = "WebSocket 연결 정보 (Dummy API)",
            description = """
                    WebSocket은 REST API가 아니므로 Swagger에 엔드포인트가 노출되지 않습니다.
                    
                    아래 정보는 프론트에서 WebSocket 연결 시 필요한 경로입니다.
                    
                    🔹 WebSocket 엔드포인트 (SockJS)
                    → /ws
                    
                    🔹 클라이언트 → 서버 메시지 전송 경로 (STOMP)
                    → /app/chat
                    
                    🔹 서버 → 클라이언트 메시지 push 경로 (구독)
                    → /topic/messages
                    
                    이 API는 문서화를 위한 더미 엔드포인트이며 실제 기능 호출은 없습니다.
                    """
    )
    @GetMapping("/api/ws-info")
    @ResponseBody
    public String wsInfo() {
        return "This is a WebSocket documentation API only.";
    }
}
