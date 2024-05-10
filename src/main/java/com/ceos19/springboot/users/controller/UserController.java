package com.ceos19.springboot.users.controller;

import com.ceos19.springboot.common.response.ApiResponse;
import com.ceos19.springboot.global.annotation.LoginUser;
import com.ceos19.springboot.jwt.TokenProvider;
import com.ceos19.springboot.users.UserDetailsImpl;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.dto.RefreshRequestDto;
import com.ceos19.springboot.users.dto.UserRequestDto;
import com.ceos19.springboot.users.dto.UserResponseDto;
import com.ceos19.springboot.users.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    //회원 가입
    @PostMapping("api/user/join")
    public ResponseEntity<ApiResponse<UserResponseDto>> userJoin(@RequestBody UserRequestDto userRequestDto) {
        userService.validateDuplicateUser(userRequestDto);
        Users savedUser = userService.createUser(userRequestDto);
        UserResponseDto userResponseDto = UserResponseDto.createFromPost(savedUser);

        ApiResponse<UserResponseDto> response = ApiResponse.of(200, "사용자 회원가입 성공", userResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("api/user/login")
    public void userLogin(@RequestBody UserRequestDto userRequestDto) {
        //userService.userLogin(userRequestDto.getLoginId(), userRequestDto.getPassword());
    }

    @PostMapping("/api/token/refresh")
    public ResponseEntity<ApiResponse<String>> refreshAccessToken(HttpServletResponse response, @RequestBody RefreshRequestDto refreshRequestDto) {
        String refreshToken = refreshRequestDto.getRefreshToken();

        if (!tokenProvider.validateAccessToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.of(401, "유효하지 않은 Refresh Token", null));
        }

        String userName = tokenProvider.getUserName(refreshToken);
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        String newAccessToken = tokenProvider.createAccessToken(userName, authentication);

        response.addHeader("Authorization", newAccessToken);

        if (newAccessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.of(401, "유효하지 않은 Refresh Token", null));
        }

        return ResponseEntity.ok(ApiResponse.of(200, "새로운 Access Token 발급", newAccessToken));
    }

    @GetMapping("/api/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> userInfo(@LoginUser UserDetailsImpl user) {
        System.out.println("user.getUsername() = " + user.getUsername());
        System.out.println("user.getUser().getLoginId() = " + user.getUser().getLoginId());
        UserResponseDto userResponseDto = UserResponseDto.createFromPost(user.getUser());
        ApiResponse<UserResponseDto> response = ApiResponse.of(200, "사용자 정보 조회", userResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
