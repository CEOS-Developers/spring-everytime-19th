package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Semester;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final ChattingRoomService chattingRoomService;
    private final CommentService commentService;
    private final TimeTableService timeTableService;

    @PostMapping("/join")

    public ResponseEntity<BaseResponse> join(@Valid @RequestBody JoinUserRequest request) {
        try {
            Long id = userService.join(request);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PostMapping("/user/{user_id}/timeTable")
    public ResponseEntity<BaseResponse> addTimeTable(@PathVariable("user_id") Long userId, @Valid @RequestBody AddTimeTableRequest request) {
        try {
            Long id = timeTableService.addTimeTable(userId, request);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
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

    @GetMapping("/user/{user_id}/posts")
    public ResponseEntity<BaseResponse<List<ReadPostResponse>>> readPosts(@PathVariable("user_id") Long userId) {
        try {
            List<ReadPostResponse> value = new ArrayList<>();

            postService.findPostByAuthorId(userId).forEach(post -> {
                value.add(ReadPostResponse.from(post));
            });

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/user/{user_id}/chattingRooms")
    public ResponseEntity<BaseResponse<List<ReadChattingRoomResponse>>> readChattingRooms(@PathVariable("user_id") Long userId) {
        try {
            List<ReadChattingRoomResponse> value = new ArrayList<>();

            chattingRoomService.findChattingRoomByParticipantId(userId).forEach(room -> {
                value.add(ReadChattingRoomResponse.from(room));
            });

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/user/{user_id}/comments")
    public ResponseEntity<BaseResponse<List<ReadCommentResponse>>> readComments(@PathVariable("user_id") Long userId) {
        try {
            List<ReadCommentResponse> value = new ArrayList<>();
            commentService.findCommentByCommenterId(userId).forEach(comment -> value.add(ReadCommentResponse.from(comment)));
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/user/{user_id}/timeTable")
    public ResponseEntity<BaseResponse<ReadTimeTableResponse>> readTimeTable(@PathVariable("user_id") Long userId,
                                                                             @RequestParam Integer year,
                                                                             @RequestParam Semester semester,
                                                                             @RequestParam String name) {
        try {
            ReadTimeTableResponse value
                    = ReadTimeTableResponse.from(
                    timeTableService
                            .findTimeTableByUserIdAndYearAndSemesterAndName(userId, year, semester, name)
            );

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/user/{user_id}/timeTables")
    public ResponseEntity<BaseResponse<List<ReadTimeTableResponse>>> readTimeTables(@PathVariable("user_id") Long userId,
                                                                                    @RequestParam(required = false) Integer year,
                                                                                    @RequestParam(required = false) Semester semester
    ) {
        try {
            List<ReadTimeTableResponse> value = new ArrayList<>();
            if (year != null && semester != null) {
                timeTableService.findTimeTableByUserIdAndYearAndSemester(userId, year, semester).forEach(table ->
                        value.add(ReadTimeTableResponse.from(table)));
            } else {
                timeTableService.findTimeTableByUserId(userId).forEach(table ->
                        value.add(ReadTimeTableResponse.from(table))
                );
            }

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

}
