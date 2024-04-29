package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Attachment;
import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.service.BoardService;
import com.ceos19.everytime.service.PostService;
import com.ceos19.everytime.service.SchoolService;
import com.ceos19.everytime.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final PostService postService;

    @PostMapping("/{board_id}/post")
    public ResponseEntity<BaseResponse> addPost(@PathVariable("board_id") Long boardId, @Valid @RequestBody AddPostRequest request) {
        try {
            Long id = postService.addPost(request, boardId);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<BaseResponse<ReadBoardResponse>> readBoard(@PathVariable("board_id") Long boardId) {
        try {
            Board board = boardService.findBoardById(boardId);
            ReadBoardResponse value = ReadBoardResponse.from(board);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @PatchMapping("/{board_id}")
    public ResponseEntity<BaseResponse> modifyBoard(@PathVariable("board_id") Long boardId, @Valid @RequestBody ModifyBoardRequest request) {
        try {
            boardService.modifyBoard(boardId, request.getName());
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/{board_id}/posts")
    public ResponseEntity<BaseResponse<List<ReadPostResponse>>> readPost(@PathVariable("board_id") Long boardId,
                                                                         @RequestParam(value = "title", required = false) String title,
                                                                         @RequestParam(value = "createdDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate createdDate) {
        try {
            List<ReadPostResponse> value = new ArrayList<>();
            if (title == null && createdDate == null) {
                postService.findPostByBoardId(boardId)
                        .forEach(p -> value.add(ReadPostResponse.from(p)));
            }
            if (title != null && createdDate == null) {
                postService.findPostByBoardIdAndTitle(boardId, title)
                        .forEach(p -> value.add(ReadPostResponse.from(p)));
            }
            if (createdDate != null && title == null) {
                postService.findPostByBoardIdAndCreatedDate(boardId, createdDate)
                        .forEach(p -> value.add(ReadPostResponse.from(p)));
            }
            if (title != null && createdDate != null) {
                log.error("에러 내용: 게시물 조회 실패 " +
                        "발생 원인: 잘못된 query parameter 사용");
                throw new AppException(ErrorCode.INVALID_URI_ACCESS, "잘못된 접근입니다.");
            }

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
