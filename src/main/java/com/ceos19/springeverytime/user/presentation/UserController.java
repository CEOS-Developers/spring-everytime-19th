package com.ceos19.springeverytime.user.presentation;

import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.dto.request.UserRequestDto;
import com.ceos19.springeverytime.user.dto.response.ResponseUserDto;
import com.ceos19.springeverytime.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "유저 컨트롤러")
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    @Operation(summary = "유저 회원가입", description = "새로운 유저를 DB에 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 형식입니다."
                    // content, schema 옵션들을 통해 상세한 에러 정보를 view에 전달할 수 있다.
            ),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 회원입니다.")
    })
    public ResponseEntity<Void> userAdd(@RequestBody final UserRequestDto request){
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping()
    @Operation(summary = "유저 목록 조회", description = "존재하는 모든 유저의 목록을 조회")
    public ResponseEntity<List<User>> userList(){
        List<User> users = userService.readAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "유저 회원탈퇴", description = "존재하는 유저의 정보를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    public ResponseEntity<Void> userRemove(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "단건 회원 조회", description = "특정 유저의 정보를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    public ResponseEntity<ResponseUserDto> userDetails(@PathVariable Long userId){
        return ResponseEntity.ok(userService.readUser(userId));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "유저정보 수정", description = "존재하는 회원정보를 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    public ResponseEntity<Void> userUpdate(@RequestBody UserRequestDto userRequestDto
            , @PathVariable Long userId){
        userService.updateUser(userRequestDto, userId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
