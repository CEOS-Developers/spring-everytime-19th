package com.ceos19.everytime.message.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos19.everytime.message.dto.request.MessageReadRequestDto;
import com.ceos19.everytime.message.dto.request.MessageRequestDto;
import com.ceos19.everytime.message.dto.response.MessageResponseDto;
import com.ceos19.everytime.message.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Message Controller", description = "메시지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "쪽지 전송", description = "쪽지를 전송합니다.")
    @PostMapping
    public void sendMessage(@RequestBody final MessageRequestDto request) {
        messageService.sendMessage(request);
    }

    @Operation(summary = "쪽지 읽기", description = "쪽지를 읽습니다.")
    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> readMessage(@RequestBody final MessageReadRequestDto request) {
        final List<MessageResponseDto> responses = messageService.readMessage(request);
        return ResponseEntity.ok().body(responses);
    }
}
