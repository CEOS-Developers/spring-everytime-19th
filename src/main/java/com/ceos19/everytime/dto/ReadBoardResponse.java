package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadBoardResponse {
    private Long id;
    private String name;

    public static ReadBoardResponse from(Board board) {
        return new ReadBoardResponse(board.getId(), board.getName());
    }
}
