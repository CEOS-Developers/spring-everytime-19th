package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.AddPostRequest;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ReadBoardResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.BoardService;
import com.ceos19.everytime.service.PostService;
import com.ceos19.everytime.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
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

    @PostMapping("/{bid}/posts")
    public BaseResponse addPost(@PathVariable("bid") Long boardId, @Valid @RequestBody AddPostRequest request) {
        try {
            Board board = boardService.findBoardById(boardId);
            User user = userService.findUserById(request.getUserId());

            Post post = Post.builder().
                    title(request.getTitle())
                    .content(request.getContent())
                    .isQuestion(request.isQuestion())
                    .isAnonymous(request.isAnonymous())
                    .board(board)
                    .author(user)
                    .build();
            Long id = postService.addPost(post);
            return new BaseResponse(HttpStatus.OK, null, id, 1);
        } catch (AppException e) {
            return new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }
}
