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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final SchoolService schoolService;
    private final BoardService boardService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/{bid}")
    public BaseResponse<ReadBoardResponse> readBoard(@PathVariable("bid") Long boardId) {
        try {
            Board board = boardService.findBoardById(boardId);
            ReadBoardResponse value = ReadBoardResponse.from(board);
            return new BaseResponse<>(HttpStatus.OK, null, value, 1);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @PatchMapping("/{bid}")
    public BaseResponse modifyBoard(@PathVariable("bid") Long boardId, @Valid @RequestBody ModifyBoardRequest request) {
        try {
            boardService.modifyBoard(boardId, request.getName());
            return new BaseResponse(HttpStatus.OK, null, null, 0);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }

    }

    @PostMapping("/{bid}/posts")
    public BaseResponse addPost(@PathVariable("bid") Long boardId, @Valid @RequestBody AddPostRequest request) {
        try {
            Board board = boardService.findBoardById(boardId);
            User user = userService.findUserById(request.getUserId());

            // Post 생성
            Post post = Post.builder().
                    title(request.getTitle())
                    .content(request.getContent())
                    .isQuestion(request.isQuestion())
                    .isAnonymous(request.isAnonymous())
                    .board(board)
                    .author(user)
                    .build();

            // Post에 첨부파일 추가
            for (AttachmentDto dto : request.getAttachments()) {
                Attachment attachment
                        = new Attachment(dto.getOriginalFileName(), dto.getStoredPath(), dto.getAttachmentType());

                post.addAttachment(attachment);
            }

            // Post 등록
            Long id = postService.addPost(post);
            return new BaseResponse(HttpStatus.OK, null, id, 1);
        } catch (AppException e) {
            return new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @GetMapping("/{bid}/posts")
    public BaseResponse<List<ReadPostResponse>> readPost(@PathVariable("bid") Long boardId,
                                                         @RequestParam(value = "title", required = false) String title,
                                                         @RequestParam(value = "createdDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate createdDate) {
        try {
            System.out.println("title = " + title);
            System.out.println("createdDate = " + createdDate);
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

            return new BaseResponse(HttpStatus.OK, null, value, value.size());
        } catch (AppException e) {
            return new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }
}
