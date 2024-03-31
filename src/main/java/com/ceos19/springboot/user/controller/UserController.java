package com.ceos19.springboot.user.controller;

import com.ceos19.springboot.common.LoginType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.common.UserRoleEnum;
import com.ceos19.springboot.common.api.ApiResponseDto;
import com.ceos19.springboot.common.api.ResponseUtils;
import com.ceos19.springboot.common.api.SuccessResponse;
import com.ceos19.springboot.user.dto.LoginRequestsDto;
import com.ceos19.springboot.user.dto.SignupRequestDto;
import com.ceos19.springboot.user.dto.TokenDto;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponseDto<TokenDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }
    // 추후에 수정 예정
    // 기존에는 SuccessResponseDto를 통해서 userrequestsdto를 통하여 주입을 해주었다
    /*
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto> login(@RequestBody UserReqestsDto reqestsDto, BindingResult bindingResult)
    {
        return userService.signup(requestDto, bindingResult);
    }
    */

    @PostMapping("/login")
    public ApiResponseDto<SuccessResponse> login(@RequestBody LoginRequestsDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }
}
