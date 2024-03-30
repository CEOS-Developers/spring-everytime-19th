package com.ceos19.springeverytime.domain.chatroom.controller;

import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomResponse;
import com.ceos19.springeverytime.domain.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms() {
        /// TEMPORARY
        Long userId = 1L;
        /// TEMPORARY
        List<ChatRoomResponse> chatRoomResponses = chatRoomService.getChatRoomsForUser(userId);
        return ResponseEntity.ok().body(chatRoomResponses);
    }
}
