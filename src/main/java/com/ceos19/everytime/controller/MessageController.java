package com.ceos19.everytime.controller;

import com.ceos19.everytime.dto.CreateResponse;
import com.ceos19.everytime.dto.CreateMessageRequest;
import com.ceos19.everytime.dto.DeleteRequest;
import com.ceos19.everytime.dto.MessageResponse;
import com.ceos19.everytime.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ceos19.everytime.exception.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;


    @PostMapping()
    public ResponseEntity<CreateResponse> createMessage (@RequestBody CreateMessageRequest createMessageRequest){
        final Long messageId = messageService.createMessage(createMessageRequest);
        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(new CreateResponse(messageId));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<MessageResponse>> findEveryMessage (@PathVariable final Long memberId) {
        final List<MessageResponse> messageResponseList = messageService.findEveryMessage(memberId);
        return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
                .body(messageResponseList);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage (@PathVariable final Long messageId, @RequestBody DeleteRequest deleteRequest) {
        messageService.deleteMessage(messageId, deleteRequest);
        return ResponseEntity.status(DELETE_SUCCESS.getHttpStatus()).build();
    }

}
