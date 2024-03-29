package com.ceos19.everytime.controller;

import com.ceos19.everytime.dto.*;
import com.ceos19.everytime.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ceos19.everytime.exception.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping()
    public ResponseEntity<CreateResponse> createBoard(@RequestBody CreateBoardRequest createBoardRequest){
        final Long boardId = boardService.create(createBoardRequest);
        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(new CreateResponse(boardId));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> findBoard(@PathVariable final Long boardId){
        final BoardResponse boardResponse = boardService.findBoard(boardId);
        return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
                .body(boardResponse);
    }

    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<BoardResponse>> findEveryBoard(@PathVariable final Long universityId){
        final List<BoardResponse> boardResponses = boardService.findBoardList(universityId);
        return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
                .body(boardResponses);
    }

    @PutMapping("{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable final Long boardId, @RequestBody BoardUpdateRequest boardUpdateRequest){
        boardService.updateBoard(boardId, boardUpdateRequest);
        return ResponseEntity.status(UPDATE_SUCCESS.getHttpStatus()).build();
    }

    @DeleteMapping("{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable final Long boardId, @RequestBody DeleteBoardRequest deleteBoardRequest) {
        boardService.delete(boardId, deleteBoardRequest);
        return ResponseEntity.status(DELETE_SUCCESS.getHttpStatus()).build();
    }



}
