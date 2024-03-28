package com.ceos19.springboot.friend.controller;

import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;
    @PostMapping("/friend/{friendId}")
    public ApiResponseDto<SuccessResponse> registerFriend(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long friendId)
    {
        return friendService.registerFriend(loginUser, friendId);
    }
    @PatchMapping("/friend/{friendId}")
    public ApiResponseDto<SuccessResponse> responseFriend(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long friendId)
    {
        return friendService.responseFriend(loginUser, friendId);
    }
    @DeleteMapping("/friend/{friendId}")
    public ApiResponseDto<SuccessResponse> deleteFriend(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long friendId)
    {
        return friendService.deleteFriend(loginUser, friendId);
    }
}
