package com.ceos19.springeverytime.message.presentation;

import com.ceos19.springeverytime.message.dto.MessageDto;
import com.ceos19.springeverytime.message.dto.ResponseMessageDto;
import com.ceos19.springeverytime.message.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message/{roomId}")
@Tag(name = "Message Controller")
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<ResponseMessageDto>> messageListByRoom(@PathVariable Long roomId) {
        messageService.getMessagesByRoom(roomId);
        return ResponseEntity.ok(messageService.getMessagesByRoom(roomId));
    }

    @PostMapping
    public ResponseEntity<Void> messageAdd(@RequestBody MessageDto request) {
        messageService.createMessage(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> messageDelete(@RequestBody MessageDto request) {
        messageService.deleteMessage(request);
        return ResponseEntity.ok().build();
    }
}
