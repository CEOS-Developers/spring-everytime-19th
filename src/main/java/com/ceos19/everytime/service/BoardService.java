package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Board create(Long memberId, String boardName, String description, Long universityId){
        Optional<Member> boardManager = memberRepository.findById(memberId);
        Optional<University> university = universityRepository.findById(universityId);

        if(boardManager.isEmpty() || university.isEmpty() || !validateBoard(boardName,description)){
            log.info("[Service][createBoard] FAIL");
            return null;
        }

        Board board = new Board(boardName,description,boardManager.get(),university.get());
        boardRepository.save(board);
        log.info("[Service][createBoard] SUCCESS");

        return board;
    }

    public void delete(Long boardId, Long memberId){
        Optional<Board> board = boardRepository.findByIdAndBoardManager(boardId,memberRepository.findById(memberId).get());

        if(board.isEmpty()){
            log.info("[Service][deleteBoard] FAIL");
        }
        else{
            boardRepository.delete(board.get());
            log.info("[Service][deleteBoard] SUCCESS");
        }
    }

    public void updateBoardName(Long boardId, String boardName){
        Optional<Board> board = boardRepository.findById(boardId);

        if(board.isEmpty() || !validateBoardName(boardName)){
            log.info("[Service][updateBoardName] FAIL");
        }
        else{
            board.get().changeBoardName(boardName);
            log.info("[Service][updateBoardName] SUCCESS");
        }
    }

    public void updateDescription(Long boardId, String description){
        Optional<Board> board = boardRepository.findById(boardId);

        if(board.isEmpty() || !validateDescription(description)){
            log.info("[Service][updateDescription] FAIL");
        }
        else{
            board.get().changeDescription(description);
            log.info("[Service][updateDescription] SUCCESS");
        }
    }


    private boolean validateBoardName(String boardName){
        if(boardName.isEmpty() || boardName.length()> MAX_NAME_LENGTH)
            return false;
        return true;
    }

    private boolean validateDescription(String description){
        if(description.isEmpty() || description.length()> MAX_DESCRIPTION_LENGTH)
            return false;
        return true;
    }

    private boolean validateBoard(String boardName, String description){
        if(!validateBoardName(boardName) || !validateDescription(description))
            return false;
        return true;
    }

}
