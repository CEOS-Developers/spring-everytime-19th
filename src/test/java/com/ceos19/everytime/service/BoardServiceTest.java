package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.UniversityRepository;
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

        //when
        board1 = boardService.create(boardManager.getId(),"맛집 추천", "맛집 탐방하는 게시판", university.getId());
        board2 = boardService.create(boardManager.getId(),"강의 추천", "강의 추천하는 게시판", university.getId());

        //then
        assertEquals(2, boardRepository.count());
    }

    @DisplayName("게시판이 올바르게 삭제된다")
    @Test
    void 게시판_삭제(){
        //given
        board1 = boardService.create(boardManager.getId(),"맛집 추천", "맛집 탐방하는 게시판", university.getId());

        //when
        boardService.delete(board1.getId(), boardManager.getId());

        //then
        assertEquals(0, boardRepository.count());
    }

}
