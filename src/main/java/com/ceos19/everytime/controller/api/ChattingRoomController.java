package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.ChattingRoom;
import com.ceos19.everytime.dto.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.ChatService;
import com.ceos19.everytime.service.ChattingRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chattingRoom")
@RequiredArgsConstructor
public class ChattingRoomController {
    private final ChattingRoomService chattingRoomService;
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<BaseResponse> addChattingRoom(@Valid @RequestBody AddChattingRoomRequest request) {
        try {
            Long id = chattingRoomService.addChattingRoom(request);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/{chattingRoom_id}")
    public ResponseEntity<BaseResponse<ReadChattingRoomResponse>> readChattingRoom(@PathVariable("chattingRoom_id") Long chattingRoomId) {
        try {
            ChattingRoom chattingRoom = chattingRoomService.findChattingRoomById(chattingRoomId);
            ReadChattingRoomResponse value = ReadChattingRoomResponse.from(chattingRoom);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PostMapping("/{chattingRoom_id}/chat")
    public ResponseEntity<BaseResponse> addChat(@PathVariable("chattingRoom_id") Long chattingRoomId, @Valid @RequestBody AddChatRequest request) {
        try {
            Long id = chatService.addChat(chattingRoomId, request);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/{chattingRoom_id}/chats")
    public ResponseEntity<BaseResponse<List<ReadChatResponse>>> readChats(@PathVariable("chattingRoom_id") Long chattingRoomId) {
        try {
            List<ReadChatResponse> value = new ArrayList<>();
            chatService.findChatByChattingRoomId(chattingRoomId).forEach(chat -> {
                value.add(ReadChatResponse.from(chat));
            });

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @DeleteMapping("/{chattingRoom_id}")
    public ResponseEntity<BaseResponse> deleteChat(@PathVariable("chattingRoom_id") Long chattingRoomId) {
        try {
            chattingRoomService.removeChattingRoom(chattingRoomId);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
