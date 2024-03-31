package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.dto.BoardResponse;
import com.ceos19.everytime.dto.BoardUpdateRequest;
import com.ceos19.everytime.dto.CreateBoardRequest;
import com.ceos19.everytime.dto.DeleteRequest;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {
    public static final int MAX_NAME_LENGTH = 20;
    public static final int MAX_DESCRIPTION_LENGTH = 50;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;

    public Long create(CreateBoardRequest createBoardRequest){
        final Member member = memberRepository.findById(createBoardRequest.getBoardManagerId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        final University university = universityRepository.findById(createBoardRequest.getBoardManagerId())
                .orElseThrow(() -> new CustomException(UNIVERSITY_NOT_FOUND));

        Board board = new Board(createBoardRequest.getBoardName(),createBoardRequest.getDescription(),member,university);

        return boardRepository.save(board)
                .getId();
    }

    @Transactional(readOnly = true)
    public BoardResponse findBoard(final Long boardId){
        final Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        //return new BoardResponse(board.getId(), board.getBoardName(),board.getDescription(), board.getBoardManager(), board.getUniversity(), board.getPosts());
        return BoardResponse.from(board);
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> findBoardList(final Long universityId){
        final University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new CustomException(UNIVERSITY_NOT_FOUND));
        List<BoardResponse> boardResponseList = new ArrayList<>();
        for(Board board : boardRepository.findAllByUniversityId(universityId)){
            boardResponseList.add(BoardResponse.from(board));
        }

        return boardResponseList;
    }

    public void updateBoard(Long boardId, BoardUpdateRequest boardUpdateRequest){
        final Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        board.changeBoardName(boardUpdateRequest.getBoardName());
        board.changeDescription(boardUpdateRequest.getDescription());
    }

    public void delete(Long boardId, DeleteRequest deleteRequest){
        final Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        final Member member = memberRepository.findById(deleteRequest.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if(board.getBoardManager().equals(member))
            boardRepository.delete(board);
        else
            throw new CustomException(INVALID_PARAMETER);
    }

}
