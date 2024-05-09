package com.ceos19.everytime.controller;

import com.ceos19.everytime.dto.member.LogInRequest;
import com.ceos19.everytime.dto.member.MemberDto;
import com.ceos19.everytime.dto.member.SignUpRequest;
import com.ceos19.everytime.security.TokenDto;
import com.ceos19.everytime.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ceos19.everytime.exception.SuccessCode.INSERT_SUCCESS;
import static com.ceos19.everytime.exception.SuccessCode.SELECT_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LogInRequest logInRequest) {

        log.info("signUp - getUsername  = {} , password = {}", logInRequest.getUsername(), logInRequest.getPassword());

        return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
                .body(memberService.login(logInRequest.getUsername(), logInRequest.getPassword()));
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpRequest signUpRequest) {

        log.info("signUp - getUsername  = {} , password = {}", signUpRequest.getUsername(),signUpRequest.getPassword());
        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(memberService.signUp(signUpRequest));
    }

}