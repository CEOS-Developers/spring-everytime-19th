package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.University;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UniversityRepository;
import jakarta.persistence.EntityManager;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class nPlus1Test {

    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired UniversityRepository universityRepository;
    @Autowired
    BoardRepository boardRepository;
    Member member1;
    University university;
    Board board1, board2;

    @Autowired
    EntityManager em;

    /*
    @BeforeEach
    void setup(){
        university = new University("홍익대학교");
        universityRepository.save(university);
        member1 = new Member("amy", "abcd123", "password", "abcd123@gmail.com", university);
        memberRepository.save(member1);
        board1 = new Board("맛집추천", "맛집 추천하는 게시판", member1,university);
        board2 = new Board("강의 후기", "강의 리뷰 적는 게시판", member1,university);
        boardRepository.save(board1);
        boardRepository.save(board2);
    }

    @DisplayName("n+1 테스트")
    @Test
    void nPlus1() throws Exception {
        //given
        Post post1 = new Post("안녕하세요", "신입생입니다...", member1, board1, false);
        Post post2 = new Post("어떤 과목", "추천해주세요", member1, board2, false);
        postRepository.save(post1);
        postRepository.save(post2);

        em.flush();
        em.clear();

        //when
        List<Post> posts = postRepository.findAll();        //    @EntityGraph(attributePaths = {"board"})

        //then
        for(Post post : posts){
            System.out.println("post = " + post.getTitle());
            System.out.println("post.getBoard().getClass() = " + post.getBoard().getClass());
            System.out.println("post.getBoard().getBoardName() = " + post.getBoard().getBoardName());
        }

    }
*/

}
