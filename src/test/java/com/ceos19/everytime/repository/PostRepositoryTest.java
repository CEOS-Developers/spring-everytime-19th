package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.University;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired UniversityRepository universityRepository;
    @Autowired BoardRepository boardRepository;

    Member member1;
    University university;
    Board board;

    @Before
    void setup(){
        university = new University("홍익대학교");
        universityRepository.save(university);
        member1 = new Member("amy", "abcd123", "password", "abcd123@gmail.com", university);
        memberRepository.save(member1);
        board = new Board("맛집추천", "맛집 추천하는 게시판", member1,university);
        boardRepository.save(board);
    }

    @DisplayName("게시글이 올바르게 작성된다")
    @Test
    public void 게시글_작성() {
        //given
        Post post = new Post("안녕하세요", "신입생입니다...", member1, board, false);

        //when
        postRepository.save(post);
        Optional<Post> test1 = postRepository.findByTitle("안녕하세요");

        //then
        assertEquals(post.getId(), test1.get().getId());
    }


}
