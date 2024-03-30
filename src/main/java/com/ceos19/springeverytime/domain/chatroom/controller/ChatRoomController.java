package com.ceos19.springeverytime.domain.chatroom.controller;

import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomDetailResponse;
import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomResponse;
import com.ceos19.springeverytime.domain.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping()
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms() {
        /// TEMPORARY
        Long userId = 1L;
        /// TEMPORARY
        List<ChatRoomResponse> chatRoomResponses = chatRoomService.getChatRoomsForUser(userId);
        return ResponseEntity.ok().body(chatRoomResponses);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomDetailResponse> getChatRoom(
            @PathVariable final Long roomId
    ) {
        ChatRoomDetailResponse response = chatRoomService.getCharRoomDetail(roomId);
        return ResponseEntity.ok().body(response);
    }
}
