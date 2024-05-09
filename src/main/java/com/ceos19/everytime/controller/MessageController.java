package com.ceos19.everytime.controller;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.dto.CreateResponse;
import com.ceos19.everytime.dto.message.CreateMessageRequest;
import com.ceos19.everytime.dto.message.MessageResponse;
import com.ceos19.everytime.security.CustomUserDetails;
import com.ceos19.everytime.security.CustomUserDetailsService;
import com.ceos19.everytime.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ceos19.everytime.exception.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<CreateResponse> createMessage (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody@Valid final CreateMessageRequest createMessageRequest){

        final Member member = userDetails.getMember();
        final Long messageId = messageService.createMessage(createMessageRequest, member.getId());

        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(CreateResponse.from(messageId));
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> findEveryMessage (@AuthenticationPrincipal CustomUserDetails userDetails) {

        final Member member = userDetails.getMember();
        final List<MessageResponse> messageResponseList = messageService.findEveryMessage(member.getId());

        return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
                .body(messageResponseList);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable final Long messageId) {

        final Member member = userDetails.getMember();
        messageService.deleteMessage(member.getId(), messageId);

        return ResponseEntity.status(DELETE_SUCCESS.getHttpStatus()).build();
    }

}
