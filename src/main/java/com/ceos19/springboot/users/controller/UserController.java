package com.ceos19.springboot.users.controller;

import com.ceos19.springboot.global.ApiResponse;
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

    @PostMapping("api/user/login")
    public ResponseEntity<ApiResponse<UserResponseDto>> userLogin(@RequestBody UserRequestDto userRequestDto) {
        try {
            Users user = Users.builder()
                    .nickname(userRequestDto.getNickname())
                    .username("")
                    .loginId(userRequestDto.getLoginId())
                    .password(userRequestDto.getPassword())
                    .email("")
                    .build();
            Long userId = userService.saveUser(user);
            UserResponseDto userResponseDto = UserResponseDto.createFromPost(user);
            ApiResponse<UserResponseDto> response = ApiResponse.of(200, "사용자 로그인 성공", userResponseDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<UserResponseDto> response = ApiResponse.of(500, "사용자 로그인 성공", null);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
