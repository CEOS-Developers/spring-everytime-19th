package com.ceos19.everytime.domain;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class PostTest {

    User user;
    Post post;

    @BeforeEach
    public void each(){
        user = new User("user", "11111", "asdf@naver.com", "asdf");
        post = new Post("post1", "content1");
    }

    @Test
    public void 댓글달기() throws Exception{
        //given

        //when

        //then

    }
}