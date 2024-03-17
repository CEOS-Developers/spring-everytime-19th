package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
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

    Member member1;
    LocalDateTime currentDateTime = LocalDateTime.now();

    @Before
    void setup(){
        member1 = new Member("amy", "abcd123", "password", "abcd123@gmail.com");
        memberRepository.save(member1);
    }

    @DisplayName("게시글이 올바르게 작성된다")
    @Test
    public void 게시글_작성() {
        //given
        Post post = new Post("안녕하세요", "신입생입니다...", currentDateTime, null, member1, false);

        //when
        postRepository.save(post);
        Optional<Post> test1 = postRepository.findByTitle("안녕하세요");

        //then
        assertEquals(post.getId(), test1.get().getId());
    }


}
