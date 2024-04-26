package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.AddUserRequest;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ReadUserParam;
import com.ceos19.everytime.dto.ReadUserResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<BaseResponse> join(@Valid @RequestBody AddUserRequest request) {
        try{
            User user = userService.addUser(request);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, user.getId(), 1));
        }catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

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
    public ResponseEntity<BaseResponse<ReadUserResponse>> readUser(@Valid @ModelAttribute ReadUserParam request) {
        try {
            if (request.getEmail() != null) {
                User user = userService.findUserByEmail(request.getEmail());
                ReadUserResponse value = ReadUserResponse.from(user);
                return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
            } else if (request.getSchool_id() != null && !request.getStudentNo().isEmpty()) {
                User user = userService.findUserBySchoolIdAndStudentNo(request.getSchool_id(), request.getStudentNo());
                ReadUserResponse value = ReadUserResponse.from(user);
                return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
            } else {
                throw new AppException(ErrorCode.INVALID_REQUEST_DATA, "잘못된 요청입니다");
            }
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<BaseResponse<List<ReadUserResponse>>> readUsers(@RequestParam("name") String name) {
        try {
            List<ReadUserResponse> value = new ArrayList<>();
            userService.findUserByName(name).forEach(user -> {
                value.add(ReadUserResponse.from(user));
            });

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

}
