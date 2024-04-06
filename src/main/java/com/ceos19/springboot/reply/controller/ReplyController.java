package com.ceos19.springboot.reply.controller;

import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.reply.dto.ReplyRequestDto;
import com.ceos19.springboot.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;
    @PostMapping("/{commentId}")
    public ApiResponseDto<SuccessResponse> createReply(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long commentId,
            @RequestBody ReplyRequestDto replyRequestDto
            )
    {
        return replyService.createReply(loginUser, replyRequestDto, commentId);
    }
}
