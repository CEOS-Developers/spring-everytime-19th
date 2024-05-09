package com.ceos19.springboot.users.controller;

import com.ceos19.springboot.common.response.ApiResponse;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.dto.UserRequestDto;
import com.ceos19.springboot.users.dto.UserResponseDto;
import com.ceos19.springboot.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원 가입
    @PostMapping("api/user/join")
    public ResponseEntity<ApiResponse<UserResponseDto>> userJoin(@RequestBody UserRequestDto userRequestDto) {
        Users savedUser = userService.createUser(userRequestDto);
        UserResponseDto userResponseDto = UserResponseDto.createFromPost(savedUser);
        ApiResponse<UserResponseDto> response = ApiResponse.of(200, "사용자 회원가입 성공", userResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("api/user/login")
    public void userLogin(@RequestBody UserRequestDto userRequestDto) {
        //userService.userLogin(userRequestDto.getLoginId(), userRequestDto.getPassword());
    }
}
