package com.ceos19.springboot.message.controller;

import com.ceos19.springboot.common.response.ApiResponse;
import com.ceos19.springboot.message.domain.Message;
import com.ceos19.springboot.message.dto.MessageResponseDto;
import com.ceos19.springboot.message.service.MessageService;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    /**
     * 목적 : 쪽지 조회
     * 성공 : 주고 받은 쪽지 내역 리턴
     * 실패 : 에러 메시지 출력
     */
    @GetMapping("api/message")
    public ResponseEntity<ApiResponse<List<MessageResponseDto>>> retreiveMessages(Long senderId) {
        try {
            Users sender = userService.findUser(senderId);
            List<Message> messageList = messageService.retreiveMessage(sender);
            List<MessageResponseDto> messageResponseDtos = new ArrayList<>();

            for (Message message : messageList) {
                MessageResponseDto messageResponseDto = MessageResponseDto.createFromMessage(sender.getUserId(), message.getReceiver().getUserId(), message.getContent()); // 각 게시글을 PostResponseDto로 변환
                messageResponseDtos.add(messageResponseDto);
            }

            ApiResponse<List<MessageResponseDto>> response = ApiResponse.of(200, "쪽지 조회 성공", messageResponseDtos);
            return ResponseEntity.ok(response); // 모든 게시글 정보를 포함하는 ApiResponse 반환
        } catch (Exception e) {
            ApiResponse<List<MessageResponseDto>> errorResponse = ApiResponse.of(500, "쪽지 조회 중 오류가 발생했습니다: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    /**
     * 목적 : 쪽지 전송
     * 성공 : 전송한 쪽지 정보 리턴
     * 실패 : 에러 메시지 출력
     */
    @PostMapping("api/message")
    public ResponseEntity<ApiResponse<MessageResponseDto>> sendMessage(Long senderId,
                                                         Long receiverId,
                                                         String content) {
        try {
            Users sender = userService.findUser(senderId);
            Users receiver = userService.findUser(receiverId);
            messageService.sendMessage(content, sender, receiver);
            MessageResponseDto messageResponseDto = MessageResponseDto.createFromMessage(senderId, receiverId, content);
            ApiResponse<MessageResponseDto> response = ApiResponse.of(200, "쪽지 전달 성공", messageResponseDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<MessageResponseDto> errorResponse = ApiResponse.of(500, "쪽지 전송 중 오류가 발생했습니다: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
