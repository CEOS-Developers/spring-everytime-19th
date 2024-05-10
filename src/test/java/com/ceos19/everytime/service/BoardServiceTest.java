package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.dto.board.CreateBoardRequest;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.UniversityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BoardServiceTest {
    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired UniversityRepository universityRepository;
    @Autowired MemberRepository memberRepository;

    Board board1, board2;
    Member boardManager;
    University university;
/*
    @BeforeEach
    void setup(){
        university = new University("홍익대학교");
        universityRepository.save(university);
        boardManager = new Member("amy", "abcd123", "password", "abcd123@gmail.com", university);
        memberRepository.save(boardManager);
    }

    @DisplayName("게시판이 올바르게 생성된다")
    @Test
    void 게시판_생성(){
        //given
        CreateBoardRequest createBoardRequest1 = new CreateBoardRequest("맛집 추천", "맛집 탐방하는 게시판", boardManager.getId(),university.getId());
        CreateBoardRequest createBoardRequest2 = new CreateBoardRequest("강의 추천", "강의 추천하는 게시판", boardManager.getId(),university.getId());

        //when
        Long boardId1 = boardService.create(createBoardRequest1);
        Long boardId2 = boardService.create(createBoardRequest2);

        //then
        assertEquals(2, boardRepository.count());
    }


    @DisplayName("잘못된 게시판 아이디로 게시판 조회")
    @Test
    void 게시판_조회() throws CustomException {
        //given
        CreateBoardRequest createBoardRequest1 = new CreateBoardRequest("맛집 추천", "맛집 탐방하는 게시판", boardManager.getId(),university.getId());
        Long boardId1 = boardService.create(createBoardRequest1);
        Long wrongId = boardId1*2;

        //when
        Assertions.assertThrows(CustomException.class, () ->{
            boardService.findBoard(wrongId);
        });

    }
*/
}
