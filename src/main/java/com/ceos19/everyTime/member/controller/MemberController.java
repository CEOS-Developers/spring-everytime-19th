package com.ceos19.everyTime.member.controller;

import com.ceos19.everyTime.member.dto.MemberSignUpDto;
import com.ceos19.everyTime.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/general")
    public ResponseEntity<Void> saveMember(@RequestBody MemberSignUpDto memberSignUpDto){
        memberService.saveMember(memberSignUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/manager")
    public ResponseEntity<Void> saveManager(@RequestBody MemberSignUpDto memberSignUpDto){
        memberService.saveManager(memberSignUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }





}
