package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ReadUserResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{user_id}")
    public ResponseEntity<BaseResponse<ReadUserResponse>> readUser(@PathVariable("user_id") Long userId) {
        try {
            User user = userService.findUserById(userId);
            ReadUserResponse value = ReadUserResponse.from(user);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<BaseResponse<ReadUserResponse>> readUser(@Valid @Email(message = "올바른 이메일 형식이 아닙니다") @RequestParam(value = "email") String email) {
        try {
            User user = userService.findUserByEmail(email);
            ReadUserResponse value = ReadUserResponse.from(user);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
