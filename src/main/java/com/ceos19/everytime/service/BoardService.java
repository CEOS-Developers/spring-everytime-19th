package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.DATA_ALREADY_EXISTED;
import static com.ceos19.everytime.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    public Long addBoard(Board board) {
        Optional<Board> optionalBoard = boardRepository.findBySchoolIdAndName(board.getSchool().getId(), board.getName());
        if (optionalBoard.isPresent()) {
            log.error("에러 내용: 게시판 등록 실패 " +
                    "발생 원인: 이미 존재하는 게시판명으로 등록 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 게시판입니다");
        }
        boardRepository.save(board);
        return board.getId();
    }

    @Transactional(readOnly = true)
    public Board findBoardById(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isEmpty()) {
            log.error("에러 내용: 게시판 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(DATA_ALREADY_EXISTED, "존재하지 않는 게시판입니다");
        }
        return optionalBoard.get();
    }

    @Transactional(readOnly = true)
    public List<Board> findBoardBySchoolId(Long schoolId) {
        return boardRepository.findBySchoolId(schoolId);
    }

    @Transactional(readOnly = true)
    public Board findBoardBySchoolIdAndName(Long schoolId, String name) {
        Optional<Board> optionalBoard = boardRepository.findBySchoolIdAndName(schoolId, name);
        if (optionalBoard.isEmpty()) {
            log.error("에러 내용: 게시판 조회 실패 " +
                    "발생 원인: 존재하지 않는 게시판명으로 조회");
            throw new AppException(DATA_ALREADY_EXISTED, "존재하지 않는 게시판입니다");
        }
        return optionalBoard.get();
    }
}
