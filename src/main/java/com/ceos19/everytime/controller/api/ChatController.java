package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ReadChatResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{chat_id}")
    public ResponseEntity<BaseResponse<ReadChatResponse>> readChat(@PathVariable Long chat_id) {
        try {
            ReadChatResponse value = ReadChatResponse.from(chatService.findChatById(chat_id));

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @DeleteMapping("/{chat_id}")
    public ResponseEntity<BaseResponse> deleteChat(@PathVariable Long chat_id) {
        try {
            chatService.removeChat(chat_id);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
