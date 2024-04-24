package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final SchoolRepository schoolRepository;

    public Board addBoard(String name, Long schoolId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> {
            log.error("에러 내용: 게시판 등록 실패 " +
                    "발생 원인: 존재하지 않는 School PK로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
        });
        boardRepository.findBySchoolIdAndName(schoolId, name).ifPresent(s -> {
            log.error("에러 내용: 게시판 등록 실패 " +
                    "발생 원인: 이미 존재하는 게시판명으로 등록 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 게시판입니다");
        });

        Board board = new Board(name, school);
        boardRepository.save(board);

        return board;
    }

    @Transactional(readOnly = true)
    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> {
            log.error("에러 내용: 게시판 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
        });
    }

    @Transactional(readOnly = true)
    public List<Board> findBoardBySchoolId(Long schoolId) {
        schoolRepository.findById(schoolId).orElseThrow(() -> {
            log.error("에러 내용: 게시판 조회 실패 " +
                    "발생 원인: 존재하지 않는 학교 PK로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
        });

        return boardRepository.findBySchoolId(schoolId);
    }

    @Transactional(readOnly = true)
    public Board findBoardBySchoolIdAndName(Long schoolId, String name) {
        return boardRepository.findBySchoolIdAndName(schoolId, name)
                .orElseThrow(() -> {
                    log.error("에러 내용: 게시판 조회 실패 " +
                            "발생 원인: 존재하지 않는 게시판명으로 조회");
                    return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
                });
    }

    public void modifyBoard(Long boardId, String name) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                            log.error("에러 내용: 게시판 조회 실패 " +
                                    "발생 원인: 존재하지 않는 PK 값으로 조회");
                            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
                        }
                );
        boardRepository.findBySchoolIdAndName(board.getId(), name)
                .ifPresent(b -> {
                    log.error("에러 내용: 게시판 수정 실패 " +
                            "발생 원인: 이미 존재하는 게시판 이름으로 수정 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 게시판 이름입니다");
                });
        board.updateName(name);
    }
}
