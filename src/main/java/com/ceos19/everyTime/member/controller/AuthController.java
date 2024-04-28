package com.ceos19.everyTime.member.controller;

import com.ceos19.everyTime.common.ApiBaseResponse;
import com.ceos19.everyTime.member.dto.SignInDto;
import com.ceos19.everyTime.member.dto.TokenRequestDto;
import com.ceos19.everyTime.member.dto.TokenResponseDto;
import com.ceos19.everyTime.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ApiBaseResponse<TokenResponseDto>> login(@RequestBody @Valid SignInDto memberSignInDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiBaseResponse.createSuccess(authService.login(memberSignInDto)));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody TokenRequestDto tokenRequestDto){
        authService.logout(tokenRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/reissue")
    public ResponseEntity<ApiBaseResponse<TokenResponseDto>> reIssue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiBaseResponse.createSuccess(authService.reIssue(tokenRequestDto)));
    }



}
