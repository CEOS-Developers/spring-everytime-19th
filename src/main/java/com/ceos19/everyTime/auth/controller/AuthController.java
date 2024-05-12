package com.ceos19.everyTime.auth.controller;

import com.ceos19.everyTime.auth.dto.AccessTokenResponseDto;
import com.ceos19.everyTime.common.ApiBaseResponse;
import com.ceos19.everyTime.auth.dto.SignInRequestDto;
import com.ceos19.everyTime.auth.dto.TokenResponseDto;
import com.ceos19.everyTime.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<ApiBaseResponse<AccessTokenResponseDto>> login(@RequestBody @Valid final SignInRequestDto memberSignInRequestDto){

        TokenResponseDto tokenResponseDto = authService.login(memberSignInRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, tokenResponseDto.getResponseCookie().toString())
                .body(ApiBaseResponse.createSuccess(AccessTokenResponseDto.builder().accessToken(tokenResponseDto.getAccessToken()).build()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("accessToken") final String accessToken
                                      ){
        authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/reissue")
    public ResponseEntity<ApiBaseResponse<AccessTokenResponseDto>> reIssue(@RequestHeader("accessToken") final String accessToken,
                                                                      @RequestHeader("refreshToken") final String refreshToken){

        TokenResponseDto tokenResponseDto = authService.reIssue(accessToken,refreshToken);


        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE,tokenResponseDto.getResponseCookie().toString())
                .body(ApiBaseResponse.createSuccess(AccessTokenResponseDto.builder().accessToken(tokenResponseDto.getAccessToken()).build()));
    }



}
