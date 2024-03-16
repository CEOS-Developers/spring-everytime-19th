package com.ceos19.everytime.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserTest{

    @Test
    public void postTest() throws Exception{
        //given
        User user = new User();
        Post post1 = new Post("1","2");
        Post post2 = new Post("1","2");

        //when
        user.addPost(post1);
        user.addPost(post2);

        //then
        Assert.assertEquals(user.getPosts().size(),2);
    }

}