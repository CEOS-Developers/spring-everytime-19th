package com.ceos19.everyTime.auth.controller;

import com.ceos19.everyTime.common.ApiBaseResponse;
import com.ceos19.everyTime.auth.dto.SignInRequestDto;
import com.ceos19.everyTime.auth.dto.TokenResponseDto;
import com.ceos19.everyTime.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ApiBaseResponse<TokenResponseDto>> login(@RequestBody @Valid final SignInRequestDto memberSignInRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiBaseResponse.createSuccess(authService.login(
                memberSignInRequestDto)));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("accessToken") final String accessToken
                                      ){
        authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/reissue")
    public ResponseEntity<ApiBaseResponse<TokenResponseDto>> reIssue(@RequestHeader("accessToken") final String accessToken,
                                                                      @RequestHeader("refreshToken") final String refreshToken){
        return ResponseEntity.status(HttpStatus.OK).body(ApiBaseResponse.createSuccess(authService.reIssue(accessToken, refreshToken)));
    }



}
