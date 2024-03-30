package com.ceos19.springeverytime.domain.chatmessage.controller;

import com.ceos19.springeverytime.domain.chatmessage.dto.request.ChatMessageCreateRequest;
import com.ceos19.springeverytime.domain.chatmessage.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms/{roomId}/message")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<Void> sendMessage(
            @PathVariable final Long roomId,
            @RequestBody @Valid final ChatMessageCreateRequest request
    ) {
        /// TEMPORARY
        final Long userId = 1L;
        /// TEMPORARY
        chatMessageService.sendMessage(roomId, userId, request);
        return ResponseEntity.ok().build();
    }
}
