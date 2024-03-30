package com.ceos19.springeverytime.domain.chatroom.controller;

import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomDetailResponse;
import com.ceos19.springeverytime.domain.chatroom.dto.response.ChatRoomResponse;
import com.ceos19.springeverytime.domain.chatroom.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping()
    public ResponseEntity<Void> createChatRoom(@RequestBody @Valid ChatRoomCreateRequest request) {
        /// TEMPORARY
        final Long userId = 1L;
        /// TEMPORARY
        ChatRoom chatRoom = chatRoomService.createChatRoom(userId, request);
        return ResponseEntity.created(URI.create("/room/" + chatRoom.getRoomId())).build();
    }

    @GetMapping("/list")
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
